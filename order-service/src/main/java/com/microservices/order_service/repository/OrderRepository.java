package com.microservices.order_service.repository;


import com.microservices.order_service.entity.Order;
import com.microservices.order_service.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByCustomerEmailOrderByOrderDateDesc(String customerEmail);
    List<Order> findByProductCodeOrderByOrderDateDesc(String productCode);

    List<Order> findByOrderStatusOrderByOrderDateDesc(OrderStatus orderStatus);

    List<Order> findByCustomerEmailAndOrderStatusOrderByOrderDateDesc(String customerEmail, OrderStatus orderStatus);

    @Query("SELECT o FROM Order o WHERE o.createdAt BETWEEN :startDate AND :endDate ORDER BY o.createdAt DESC")
    List<Order> findOrdersBetweenDates(@Param("startDate") LocalDateTime startDate,
                                       @Param("endDate") LocalDateTime endDate);
    long countByOrderStatus(OrderStatus orderStatus);
}