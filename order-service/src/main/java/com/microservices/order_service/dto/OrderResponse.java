package com.microservices.order_service.dto;

import com.microservices.order_service.entity.OrderStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class OrderResponse {

    private Long orderId;
    private String customerName;
    private String customerEmail;
    private String productCode;
    private String productName;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal totalAmount;
    private OrderStatus orderStatus;
    private LocalDateTime orderDate;
    private String message;
}