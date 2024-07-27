package uz.com.railway_reservation.model.dto.order;

import jakarta.persistence.Column;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class OrderDto {

    private String startTime;
    private String endTime;
    private String wagonId;
    private String fromWhere;
    private String toWhere;

}
