package uz.com.railway_reservation.model.entity.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import uz.com.railway_reservation.model.BaseModel;
import uz.com.railway_reservation.model.entity.user.UserEntity;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity(name = "orders")
public class OrderEntity extends BaseModel {

    @ManyToOne
    private UserEntity owner;

    @Column(nullable = false)
    private String fromWhere;

    @Column(nullable = false)
    private String toWhere;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime startTime;

    @Column(nullable = false)
    private UUID wagonId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endTime;

    @Column(columnDefinition = "boolean default false")
    protected boolean isCancel;

    private Double price;

    private UUID changeStatusBy;
}
