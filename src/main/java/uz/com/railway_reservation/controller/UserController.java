package uz.com.railway_reservation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.com.railway_reservation.model.dto.user.AdminDto;
import uz.com.railway_reservation.model.dto.user.UserDto;
import uz.com.railway_reservation.model.dto.user.UserForFront;
import uz.com.railway_reservation.model.entity.order.OrderEntity;
import uz.com.railway_reservation.model.entity.user.UserEntity;
import uz.com.railway_reservation.response.StandardResponse;
import uz.com.railway_reservation.service.UserService;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public StandardResponse<String> delete(
            @RequestParam UUID id,
            Principal principal
            ){
      return userService.delete(id, principal);
    }

    @GetMapping("/get-by-email")
    @PreAuthorize("hasRole('ADMIN')")
    public StandardResponse<UserForFront> getByEmail(
            @RequestParam String email
    ){
        return userService.getByEmail(email);
    }

    @GetMapping("/get-by-id")
    @PreAuthorize("hasRole('ADMIN')")
    public StandardResponse<UserForFront> getById(
            @RequestParam UUID id
    ){
        return userService.getById(id);
    }

    @GetMapping("/get-all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserEntity> getAll(){
        return userService.getAll();
    }

    @GetMapping("/get-by-number")
    @PreAuthorize("hasRole('ADMIN')")
    public StandardResponse<UserForFront> getByNumber(
            @RequestParam String number
    ){
        return userService.getByNumber(number);
    }

    @PostMapping("/change-role-to-admin-or-add-new-admin")
    public StandardResponse<UserForFront> addAdmin(
            @RequestBody AdminDto adminDto,
            Principal principal
            ){
        return userService.addAdmin(adminDto, principal);
    }

    @PutMapping("/update-profile")
    public StandardResponse<UserForFront> update(
            @RequestParam UUID id,
            @RequestBody UserDto userDto,
            Principal principal
            ){
      return  userService.update(userDto, principal,id);
    }

    @GetMapping("/get-my-orders")
    public List<OrderEntity> getMyOrders(
            @RequestParam UUID id
    ){
        return userService.getMyOrders(id);
    }
}
