package uz.com.railway_reservation.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import uz.com.railway_reservation.exception.DataHasAlreadyExistException;
import uz.com.railway_reservation.exception.DataNotFoundException;
import uz.com.railway_reservation.exception.NotAcceptableException;
import uz.com.railway_reservation.model.dto.wagon.WagonDto;
import uz.com.railway_reservation.model.dto.wagon.WagonForFront;
import uz.com.railway_reservation.model.entity.order.OrderEntity;
import uz.com.railway_reservation.model.entity.user.UserEntity;
import uz.com.railway_reservation.model.entity.wagon.WagonEntity;
import uz.com.railway_reservation.model.entity.wagon.WagonType;
import uz.com.railway_reservation.repository.OrderRepository;
import uz.com.railway_reservation.repository.UserRepository;
import uz.com.railway_reservation.repository.WagonRepository;
import uz.com.railway_reservation.response.StandardResponse;
import uz.com.railway_reservation.response.Status;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class WagonService {
    private final ModelMapper modelMapper;
    private final WagonRepository wagonRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public StandardResponse<WagonForFront> save(WagonDto wagonDto, Principal principal){
        UserEntity userEntity = userRepository.findUserEntityByEmail(principal.getName());
        checkHasWagon(wagonDto.getNumber());
        WagonEntity wagon = modelMapper.map(wagonDto, WagonEntity.class);
        wagon.setCapacity(wagon.getCapacity());
        try{
        wagon.setType(WagonType.valueOf(wagonDto.getType()));
        }catch (Exception e){
           throw new NotAcceptableException("Invalid wagon type!");
        }
        wagon.setNumber(wagonDto.getNumber());
        wagon.setCreatedBy(userEntity.getId());
        wagon.setDescription(wagonDto.getDescription());
        wagon.setPrice(wagon.getType().getAmount());
        wagon.setCapacity(wagonDto.getCapacity());
        WagonEntity wagonEntity = wagonRepository.save(wagon);
        WagonForFront wagonForFront = modelMapper.map(wagonEntity, WagonForFront.class);
        return StandardResponse.<WagonForFront>builder()
                .status(Status.SUCCESS)
                .message("Wagon added!")
                .data(wagonForFront)
                .build();
    }
    public void checkHasWagon(String number){
        WagonEntity wagon = wagonRepository.findWagonEntityByNumber(number);
        if (wagon!=null){
            throw new DataHasAlreadyExistException("Wagon has already added!");
        }
    }
    public StandardResponse<String> delete(UUID id, Principal principal){
        UserEntity user = userRepository.findUserEntityByEmail(principal.getName());
        WagonEntity wagon= wagonRepository.findWagonEntityById(id);
        if (wagon==null){
            throw new DataNotFoundException("Wagon not found!");
        }
        List<OrderEntity> orderEntities = orderRepository.findOrderEntityByWagonId(wagon.getId());
        if (orderEntities!=null){
            for (OrderEntity order:orderEntities) {
                if (order.getStartTime().isAfter(LocalDateTime.now()) || order.getStartTime().isEqual(LocalDateTime.now())
                || order.getEndTime().isAfter(LocalDateTime.now())){
                    throw new NotAcceptableException("You can not delete wagon. Because it has order!");
                }
            }
        }
        wagon.setDeleted(true);
        wagon.setDeletedBy(user.getId());
        wagon.setDeletedTime(LocalDateTime.now());
        wagonRepository.save(wagon);
        return StandardResponse.<String>builder()
                .status(Status.SUCCESS)
                .message("Wagon deleted!")
                .data("DELETED")
                .build();
    }

    public List<WagonEntity> getByType(String type){
        List<WagonEntity> wagon = wagonRepository.findWagonEntityByType(type);
        if (wagon==null){
            throw new DataNotFoundException("Wagon not found same this type!");
        }
        return wagon;
    }

    public List<WagonEntity> getAll(){
        List<WagonEntity> wagonEntities = wagonRepository.getAll();
        if (wagonEntities==null){
            throw new DataNotFoundException("Wagons not found!");
        }
        return wagonEntities;
    }

    public StandardResponse<WagonForFront> updateWagon(WagonDto wagonDto, Principal principal){
        WagonEntity wagon = wagonRepository.findWagonEntityByNumber(wagonDto.getNumber());
        UserEntity user = userRepository.findUserEntityByEmail(principal.getName());
        if (wagon==null){
            throw new DataNotFoundException("Wagon not found!");
        }
        wagon.setUpdatedTime(LocalDateTime.now());
        wagon.setUpdatedBy(user.getId());
        wagon.setCapacity(wagonDto.getCapacity());
        try{
            wagon.setType(WagonType.valueOf(wagonDto.getType()));
        }catch (Exception e){
            throw new NotAcceptableException("Invalid wagon type!");
        }
        wagon.setNumber(wagonDto.getNumber());
        wagon.setDescription(wagonDto.getDescription());
        wagon.setPrice(wagon.getType().getAmount());
        WagonForFront wagonForFront = modelMapper.map(wagon, WagonForFront.class);
        return StandardResponse.<WagonForFront>builder()
                .status(Status.SUCCESS)
                .message("Wagon updated!")
                .data(wagonForFront)
                .build();
    }
}
