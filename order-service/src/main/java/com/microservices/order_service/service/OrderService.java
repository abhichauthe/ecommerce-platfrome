package com.microservices.order_service.service;

import com.microservices.order_service.client.InventoryClient;
import com.microservices.order_service.dto.InventoryResponse;
import com.microservices.order_service.dto.OrderRequest;
import com.microservices.order_service.dto.OrderResponse;
import com.microservices.order_service.entity.Order;
import com.microservices.order_service.entity.OrderStatus;
import com.microservices.order_service.exception.InsufficientStockException;
import com.microservices.order_service.exception.OrderNotFoundException;
import com.microservices.order_service.exception.ProductNotAvailableException;
import com.microservices.order_service.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;

    @Autowired
    public OrderService(OrderRepository orderRepository, InventoryClient inventoryClient) {
        this.orderRepository = orderRepository;
        this.inventoryClient = inventoryClient;
    }

    public OrderResponse placeOrder(OrderRequest orderRequest) {
        try {
            // Step 1: Check inventory
            ResponseEntity<InventoryResponse> inventoryResponse =
                    inventoryClient.checkStock(orderRequest.getProductCode());

            if (!inventoryResponse.getStatusCode().is2xxSuccessful() ||
                    inventoryResponse.getBody() == null) {
                throw new ProductNotAvailableException(
                        "Unable to check product availability for: " + orderRequest.getProductCode());
            }

            InventoryResponse inventory = inventoryResponse.getBody();

            // Step 2: Check stock
            if (!inventory.isInStock()) {
                throw new InsufficientStockException(
                        "Product " + orderRequest.getProductCode() + " is out of stock");
            }

            if (inventory.getAvailableQuantity() < orderRequest.getQuantity()) {
                throw new InsufficientStockException(
                        "Not enough stock. Available: " + inventory.getAvailableQuantity() +
                                ", Requested: " + orderRequest.getQuantity());
            }

            // Step 3: Create order (FIXED - use default constructor and setters)
            Order order = new Order();
            order.setCustomerName(orderRequest.getCustomerName());
            order.setCustomerEmail(orderRequest.getCustomerEmail());
            order.setProductCode(orderRequest.getProductCode());
            order.setProductName(inventory.getProductName());
            order.setQuantity(orderRequest.getQuantity());
            order.setPrice(orderRequest.getPrice());

            // Calculate total amount
            BigDecimal totalAmount = orderRequest.getPrice().multiply(new BigDecimal(orderRequest.getQuantity()));
            order.setTotalAmount(totalAmount);

            // Set status and date
            order.setOrderStatus(OrderStatus.CONFIRMED);
            order.setOrderDate(LocalDateTime.now());

            // Step 4: Save order
            Order savedOrder = orderRepository.save(order);

            // Step 5: Return response
            return new OrderResponse(
                    savedOrder.getId(),
                    savedOrder.getCustomerName(),
                    savedOrder.getCustomerEmail(),
                    savedOrder.getProductCode(),
                    savedOrder.getProductName(),
                    savedOrder.getQuantity(),
                    savedOrder.getPrice(),
                    savedOrder.getTotalAmount(),
                    savedOrder.getOrderStatus(),
                    savedOrder.getOrderDate(),
                    "Order placed successfully"
            );

        } catch (ProductNotAvailableException | InsufficientStockException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to place order: " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Order getOrderById(Long orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent()) {
            return order.get();
        } else {
            throw new OrderNotFoundException("Order with ID " + orderId + " not found");
        }
    }

    @Transactional(readOnly = true)
    public List<Order> getOrdersByCustomerEmail(String customerEmail) {
        return orderRepository.findByCustomerEmailOrderByOrderDateDesc(customerEmail);
    }

    public Order updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);

        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.setOrderStatus(newStatus);
            order.setUpdatedAt(LocalDateTime.now());
            return orderRepository.save(order);
        } else {
            throw new OrderNotFoundException("Order with ID " + orderId + " not found");
        }
    }

    public Order cancelOrder(Long orderId) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);

        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();

            if (order.getOrderStatus() == OrderStatus.DELIVERED ||
                    order.getOrderStatus() == OrderStatus.CANCELLED) {
                throw new IllegalStateException("Cannot cancel order with status: " + order.getOrderStatus());
            }

            order.setOrderStatus(OrderStatus.CANCELLED);
            order.setUpdatedAt(LocalDateTime.now());
            return orderRepository.save(order);
        } else {
            throw new OrderNotFoundException("Order with ID " + orderId + " not found");
        }
    }
}