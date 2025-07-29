package com.microservice.inventory_service.service;

import com.microservice.inventory_service.dto.InventoryResponse;
import com.microservice.inventory_service.entity.Inventory;
import com.microservice.inventory_service.repository.InventoryRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    @Autowired
    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }
    public InventoryResponse checkStock(String productCode) {
        Optional<Inventory> inventory = inventoryRepository.findByProductCode(productCode);

        if (inventory.isPresent()) {
            Inventory inv = inventory.get();
            boolean inStock = inv.getQuantity() > 0;
            return InventoryResponse.builder()
                    .productCode(inv.getProductCode())
                    .productName(inv.getProductName())
                    .inStock(inStock)
                    .availableQuantity(inv.getQuantity())
                    .build();
        } else {
            return InventoryResponse.builder()
                    .productCode(productCode)
                    .inStock(false)
                    .availableQuantity(0)
                    .message("Product not found in inventory")
                    .build();
        }

    }
    @Transactional(readOnly = true)
    public List<Inventory> getAllInventory() {
        return inventoryRepository.findAll();
    }
    public Inventory addInventory(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }
    public Inventory updateQuantity(String productCode, Integer quantity) {
        Optional<Inventory> inventoryOpt = inventoryRepository.findByProductCode(productCode);

        if (inventoryOpt.isPresent()) {
            Inventory inventory = inventoryOpt.get();
            inventory.setQuantity(quantity);
            return inventoryRepository.save(inventory);
        } else {
            throw new RuntimeException("Product with code " + productCode + " not found");
        }
    }
    public Inventory reduceQuantity(String productCode, Integer quantityToReduce) {
        Optional<Inventory> inventoryOpt = inventoryRepository.findByProductCode(productCode);

        if (inventoryOpt.isPresent()) {
            Inventory inventory = inventoryOpt.get();

            if (inventory.getQuantity() >= quantityToReduce) {
                inventory.setQuantity(inventory.getQuantity() - quantityToReduce);
                return inventoryRepository.save(inventory);
            } else {
                throw new RuntimeException("Insufficient stock. Available: " + inventory.getQuantity() +
                        ", Requested: " + quantityToReduce);
            }
        } else {
            throw new RuntimeException("Product with code " + productCode + " not found");
        }
    }
}