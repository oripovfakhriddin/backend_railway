package uz.com.railway_reservation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.com.railway_reservation.exception.RequestValidationException;
import uz.com.railway_reservation.model.dto.user.UserDto;
import uz.com.railway_reservation.response.JwtResponse;
import uz.com.railway_reservation.response.StandardResponse;
import uz.com.railway_reservation.service.UserService;
import uz.com.railway_reservation.model.dto.user.LoginDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final UserService userService;
    @PostMapping("/sign-up")
    public StandardResponse<JwtResponse> signUp(
            @RequestBody UserDto userDto,
            BindingResult bindingResult
    ) throws RequestValidationException {
        if (bindingResult.hasErrors()){
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            throw new RequestValidationException(allErrors);
        }
        return userService.signUp(userDto);
    }

    @PostMapping("/sign-in")
    public StandardResponse<JwtResponse> login(
            @RequestBody LoginDto loginDto
    ){
        return userService.login(loginDto);
    }
}
