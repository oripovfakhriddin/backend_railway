package uz.com.railway_reservation.model.dto.wagon;

import lombok.*;
import uz.com.railway_reservation.model.entity.wagon.WagonType;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class WagonForFront {

    private UUID id;

    private WagonType type;

    private String number;

    private Double price;

    private String description;

}
