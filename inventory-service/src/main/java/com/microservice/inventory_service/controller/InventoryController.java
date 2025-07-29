package com.microservice.inventory_service.controller;

import com.microservice.inventory_service.InventoryServiceApplication;
import com.microservice.inventory_service.dto.InventoryResponse;
import com.microservice.inventory_service.entity.Inventory;
import com.microservice.inventory_service.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    @Autowired
    public InventoryController(InventoryService inventoryService){
        this.inventoryService = inventoryService;
    }

    @GetMapping("/check/{productCode}")
    public ResponseEntity<InventoryResponse> checkStock(@PathVariable String productCode) {
        try {
            InventoryResponse response = inventoryService.checkStock(productCode);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            InventoryResponse errorResponse = InventoryResponse.builder()
                    .productCode(productCode)
                    .inStock(false)
                    .availableQuantity(0)
                    .message("Error checking stock: " + e.getMessage())
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);

        }
    }
    @GetMapping("/all")
    public ResponseEntity<List<Inventory>> getAllInventory() {
        try {
            List<Inventory> inventories = inventoryService.getAllInventory();
            return ResponseEntity.ok(inventories);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Inventory> addInventory(@Valid @RequestBody Inventory inventory) {
        try {
            Inventory savedInventory = inventoryService.addInventory(inventory);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedInventory);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/update/{productCode}")
    public ResponseEntity<Inventory> updateQuantity(
            @PathVariable String productCode,
            @RequestParam Integer quantity) {
        try {
            Inventory updatedInventory = inventoryService.updateQuantity(productCode, quantity);
            return ResponseEntity.ok(updatedInventory);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PutMapping("/reduce/{productCode}")
    public ResponseEntity<Inventory> reduceQuantity(
            @PathVariable String productCode,
            @RequestParam Integer quantity) {
        try {
            Inventory updatedInventory = inventoryService.reduceQuantity(productCode, quantity);
            return ResponseEntity.ok(updatedInventory);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
