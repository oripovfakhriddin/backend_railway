package uz.com.railway_reservation.model.dto.order;

import lombok.*;
import uz.com.railway_reservation.model.entity.user.UserEntity;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class OrderForFront {

    private UUID id;

    private UUID wagonId;

    private LocalDateTime startTime;

    private String fromWhere;

    private String toWhere;

    private LocalDateTime endTime;

    private UserEntity user;

}
