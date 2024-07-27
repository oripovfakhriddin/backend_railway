package uz.com.railway_reservation.model.dto.user;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class UserDto {

    private String fullName;

    private String email;

    private String password;

    private String number;

    private String gender;

}
