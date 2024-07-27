package uz.com.railway_reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.com.railway_reservation.model.entity.order.OrderEntity;

import java.util.List;
import java.util.UUID;
@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {
    @Query("select u from orders as u where u.isDeleted=false and u.id=?1")
    OrderEntity findOrderEntityById(UUID id);
    @Query("select u from orders as u where u.isDeleted=false and u.wagonId=?1")
    List<OrderEntity> findOrderEntityByWagonId(UUID id);
    @Query("select u from orders as u where u.isDeleted=false and u.isCancel=true")
    List<OrderEntity> findOrderEntityByCancel();
    @Query("select u from orders as u where u.isDeleted=false")
    List<OrderEntity> getAll();
    @Query("select u from orders as u where u.isDeleted=false and u.createdBy=?1")
    List<OrderEntity> findOrderEntityByCreatedBy(UUID id);
}
