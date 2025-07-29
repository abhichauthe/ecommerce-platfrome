package com.microservice.inventory_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "inventories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Product code cannot be blank")
    @Column(name = "product_code",unique = true,nullable = false)
    private  String productCode;

    @NotBlank(message = "Product name cannot be blank")
    @Column(name = "product_name",nullable = false)
    private String productName;

    @NotNull(message = "Quantity cannot be null")
    @Min(value = 0, message = "Quantity cannot be negative")
    @Column(name = "quantity",nullable = false)
    private Integer quantity;
}
