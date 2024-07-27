package uz.com.railway_reservation.model.dto.user;

import lombok.*;
import uz.com.railway_reservation.model.entity.user.Gender;
import uz.com.railway_reservation.model.entity.user.UserRole;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class UserForFront {

    private UUID id;

    private String fullName;

    private String email;

    private String number;

    private UserRole role;

    private Gender gender;
}
