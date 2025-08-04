package com.microservice.inventory_service.dto;


import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class InventoryResponse {
    private String productCode;
    private String productName;
    private boolean inStock;
    private Integer availableQuantity;
    private String message;

}
