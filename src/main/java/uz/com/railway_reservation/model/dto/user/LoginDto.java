package uz.com.railway_reservation.model.dto.user;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class LoginDto {
    private String email;
    private String password;
}
