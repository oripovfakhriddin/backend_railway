package uz.com.railway_reservation.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@MappedSuperclass
public class BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected UUID id;

    @CreationTimestamp
    protected LocalDateTime createdTime;

    @UpdateTimestamp
    protected LocalDateTime updatedTime;

    @Column(columnDefinition = "boolean default false")
    protected boolean isDeleted;

    protected UUID createdBy;

    protected UUID deletedBy;

    protected LocalDateTime deletedTime;
}
