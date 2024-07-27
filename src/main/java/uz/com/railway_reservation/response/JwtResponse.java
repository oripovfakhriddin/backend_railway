package uz.com.railway_reservation.response;

import lombok.*;
import uz.com.railway_reservation.model.dto.user.UserForFront;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class JwtResponse {

    private String accessToken;

    private String refreshToken;

    private UserForFront user;
}
