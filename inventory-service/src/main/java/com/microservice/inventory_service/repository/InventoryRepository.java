package com.microservice.inventory_service.repository;

import com.microservice.inventory_service.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory,Long> {

    // Here we are finding inventory by product code
    Optional<Inventory> findByProductCode(String productCode);

    @Query("SELECT CASE WHEN i.quantity > 0 THEN true ELSE false END FROM Inventory i WHERE i.productCode = :productCode")
    boolean isProductInStock(@Param("productCode") String productCode);

}
