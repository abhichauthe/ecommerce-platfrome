package com.microservices.order_service.client;

import com.microservices.order_service.dto.InventoryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "inventory-service", url = "${inventory.service.url:http://localhost:8081}")
public interface InventoryClient {

    @GetMapping("/api/inventory/check/{productCode}")
    ResponseEntity<InventoryResponse> checkStock(@PathVariable("productCode") String productCode);
}
