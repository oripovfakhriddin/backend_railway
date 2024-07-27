package uz.com.railway_reservation.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.com.railway_reservation.exception.AuthenticationFailedException;
import uz.com.railway_reservation.exception.DataNotFoundException;
import uz.com.railway_reservation.exception.NotAcceptableException;
import uz.com.railway_reservation.exception.UserBadRequestException;
import uz.com.railway_reservation.model.dto.user.AdminDto;
import uz.com.railway_reservation.model.dto.user.LoginDto;
import uz.com.railway_reservation.model.dto.user.UserDto;
import uz.com.railway_reservation.model.dto.user.UserForFront;
import uz.com.railway_reservation.model.entity.order.OrderEntity;
import uz.com.railway_reservation.model.entity.user.Gender;
import uz.com.railway_reservation.model.entity.user.UserEntity;
import uz.com.railway_reservation.model.entity.user.UserRole;
import uz.com.railway_reservation.repository.OrderRepository;
import uz.com.railway_reservation.repository.UserRepository;
import uz.com.railway_reservation.response.JwtResponse;
import uz.com.railway_reservation.response.StandardResponse;
import uz.com.railway_reservation.response.Status;
import uz.com.railway_reservation.service.auth.JwtService;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final JwtService jwtService;

    public StandardResponse<JwtResponse> signUp(UserDto userDto){
        checkUserEmailAndPhoneNumber(userDto.getEmail(),userDto.getNumber());
        UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
        userEntity.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userEntity.setRole(UserRole.USER);
        userEntity.setFullName(userDto.getFullName());
        userEntity.setEmail(userDto.getEmail());
        try{
        userEntity.setGender(Gender.valueOf(userDto.getGender()));
        }catch (Exception e){
            throw new NotAcceptableException("Invalid gender type!");
        }
        userEntity.setNumber(userDto.getNumber());
        userEntity=userRepository.save(userEntity);
        userEntity.setCreatedBy(userEntity.getId());
        userRepository.save(userEntity);
        String accessToken = jwtService.generateAccessToken(userEntity);
        String refreshToken = jwtService.generateRefreshToken(userEntity);
        UserForFront user = modelMapper.map(userEntity, UserForFront.class);
        JwtResponse jwtResponse = JwtResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(user)
                .build();
        return StandardResponse.<JwtResponse>builder()
                .status(Status.SUCCESS)
                .message("Successfully signed Up")
                .data(jwtResponse)
                .build();
    }
    private void checkUserEmailAndPhoneNumber(String email, String phoneNumber) {
        UserEntity userEntity = userRepository.findUserEntityByEmail(email);
        if (userEntity!=null){
            throw new UserBadRequestException("Email has already exist!");
        }
        if (userRepository.findUserEntityByNumber(phoneNumber).isPresent()){
            throw new UserBadRequestException("Number has already exist!");
        }
    }

    public StandardResponse<JwtResponse> login(LoginDto loginDto){
        UserEntity userEntity = userRepository.findUserEntityByEmail(loginDto.getEmail());
        if (userEntity == null){
            throw new DataNotFoundException("User not found!");
        }
        if (passwordEncoder.matches(loginDto.getPassword(), userEntity.getPassword())){
            String accessToken= jwtService.generateAccessToken(userEntity);
            String refreshToken= jwtService.generateRefreshToken(userEntity);
            UserForFront user = modelMapper.map(userEntity, UserForFront.class);
            JwtResponse jwtResponse= JwtResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .user(user)
                    .build();
            return StandardResponse.<JwtResponse>builder()
                    .status(Status.SUCCESS)
                    .message("Sign in successfully!")
                    .data(jwtResponse)
                    .build();
        }
        else{
            throw new AuthenticationFailedException("Something error during signed in!");
        }
    }
    public StandardResponse<String> delete(UUID id, Principal principal){
        UserEntity user = userRepository.findUserEntityByEmail(principal.getName());
        List<OrderEntity> orderEntities= orderRepository.findOrderEntityByCreatedBy(id);
        for (OrderEntity order: orderEntities) {
            if (order.getEndTime().isAfter(LocalDateTime.now())){
                throw new NotAcceptableException("Can not delete this user. Because user has active order!");
            }
        }
        UserEntity userEntity = userRepository.findUserEntityById(id);
        if (userEntity==null){
            throw new DataNotFoundException("User not found!");
        }
        userEntity.setDeleted(true);
        userEntity.setDeletedBy(user.getId());
        userEntity.setDeletedTime(LocalDateTime.now());
        userRepository.save(userEntity);
        return StandardResponse.<String>builder()
                .data("User deleted successfully!")
                .status(Status.SUCCESS)
                .message("DELETED")
                .build();
    }

    public StandardResponse<UserForFront> getByEmail(String email){
        UserEntity userEntity = userRepository.findUserEntityByEmail(email);
        if (userEntity==null){
            throw new DataNotFoundException("User not found!");
        }
        UserForFront user = modelMapper.map(userEntity, UserForFront.class);
        return StandardResponse.<UserForFront>builder()
                .status(Status.SUCCESS)
                .data(user)
                .message("This is user")
                .build();
    }


    public StandardResponse<UserForFront> getById(UUID id){
        UserEntity userEntity = userRepository.findUserEntityById(id);
        if (userEntity==null){
            throw new DataNotFoundException("User not found!");
        }
        UserForFront user = modelMapper.map(userEntity, UserForFront.class);
        return StandardResponse.<UserForFront>builder()
                .status(Status.SUCCESS)
                .data(user)
                .message("This is user")
                .build();
    }

    public List<UserEntity> getAll(){
        List<UserEntity> userEntities = userRepository.getAll();
        if (userEntities==null){
            throw new DataNotFoundException("Users not found!");
        }
        return userEntities;
    }

    public StandardResponse<UserForFront> getByNumber(String number){
        Optional<UserEntity> userEntity = userRepository.findUserEntityByNumber(number);
        if (userEntity.isEmpty()){
            throw new DataNotFoundException("User not found!");
        }
        UserForFront user = modelMapper.map(userEntity, UserForFront.class);
        return StandardResponse.<UserForFront>builder()
                .status(Status.SUCCESS)
                .data(user)
                .message("This is user!")
                .build();
    }
    public StandardResponse<UserForFront> addAdmin(AdminDto adminDto, Principal principal){
        UserEntity userEntity=  userRepository.findUserEntityByEmail(adminDto.getEmail());
        UserEntity user = userRepository.findUserEntityByEmail(principal.getName());
        if (userEntity==null){
            throw new DataNotFoundException("User not found same this email!");
        }
        userEntity.setRole(UserRole.ADMIN);
        userEntity.setChangeRoleBy(user.getId());
        UserEntity save = userRepository.save(userEntity);
        UserForFront userForFront = modelMapper.map(save, UserForFront.class);
        return StandardResponse.<UserForFront>builder()
                .status(Status.SUCCESS)
                .data(userForFront)
                .message("Admin added!")
                .build();
    }

    public StandardResponse<UserForFront> update(UserDto userDto,Principal principal,UUID id){
        UserEntity user = userRepository.findUserEntityByEmail(principal.getName());
        UserEntity userEntity = userRepository.findUserEntityById(id);
        if (userEntity==null){
            throw new DataNotFoundException("User not found!");
        }
        userEntity.setUpdatedTime(LocalDateTime.now());
        userEntity.setFullName(userDto.getFullName());
        userEntity.setPassword(passwordEncoder.encode(userDto.getPassword()));
        try{
        userEntity.setGender(Gender.valueOf(userDto.getGender()));
        }catch (Exception e){
            throw new NotAcceptableException("Invalid gender type!");
        }
        userEntity.setUpdatedBy(user.getId());
        userEntity.setEmail(userDto.getEmail());
        userEntity.setNumber(userDto.getNumber());
        UserEntity save = userRepository.save(userEntity);
        UserForFront userForFront = modelMapper.map(save, UserForFront.class);
        return StandardResponse.<UserForFront>builder()
                .status(Status.SUCCESS)
                .data(userForFront)
                .message("Profile updated!")
                .build();
    }

    public List<OrderEntity> getMyOrders(UUID id){
        List<OrderEntity> orderEntities = orderRepository.findOrderEntityByCreatedBy(id);
        if (orderEntities.isEmpty()){
            throw new DataNotFoundException("Orders not found!");
        }
        return orderEntities;
    }
}
