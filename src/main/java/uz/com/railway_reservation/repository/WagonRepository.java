package uz.com.railway_reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.com.railway_reservation.model.entity.wagon.WagonEntity;


import java.util.List;
import java.util.UUID;

@Repository
public interface WagonRepository extends JpaRepository<WagonEntity, UUID> {
    @Query("select u from wagons as u where u.isDeleted=false and u.number=?1")
    WagonEntity findWagonEntityByNumber(String number);
    @Query("select u from wagons as u where u.isDeleted=false and u.id=?1")
    WagonEntity findWagonEntityById(UUID id);
    @Query("select u from wagons as u where u.isDeleted=false and u.type=?1")
    List<WagonEntity>findWagonEntityByType(String type);
    @Query("select u from wagons as u where u.isDeleted=false")
    List<WagonEntity> getAll();
}
