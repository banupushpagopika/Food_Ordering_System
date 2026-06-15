package com.foodsystem.backend.repository;

import com.foodsystem.backend.entity.Order;
import com.foodsystem.backend.entity.OrderStatus;
import com.foodsystem.backend.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserOrderByOrderDateDesc(User user);
    List<Order> findByOrderStatus(OrderStatus orderStatus);

    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.orderStatus <> 'CANCELLED'")
    Double calculateTotalRevenue();

    @Query("SELECT COUNT(o) FROM Order o WHERE o.orderStatus = 'PENDING'")
    Long countPendingOrders();

    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.orderStatus <> 'CANCELLED' AND o.orderDate BETWEEN :start AND :end")
    Double calculateSalesBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.orderStatus <> 'CANCELLED' AND o.orderDate BETWEEN :start AND :end")
    Long countOrdersBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT oi.menuItem.id, oi.menuItem.itemName, SUM(oi.quantity), SUM(oi.price * oi.quantity) " +
           "FROM OrderItem oi JOIN oi.order o " +
           "WHERE o.orderStatus <> 'CANCELLED' " +
           "GROUP BY oi.menuItem.id, oi.menuItem.itemName " +
           "ORDER BY SUM(oi.quantity) DESC")
    List<Object[]> findTopSellingFoods(Pageable pageable);
}
