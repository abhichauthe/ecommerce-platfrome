package com.microservices.order_service.entity;

public enum OrderStatus {
    PENDING("Order is pending processing"),
    CONFIRMED("Order has been confirmed"),
    PROCESSING("Order is being processed"),
    SHIPPED("Order has been shipped"),
    DELIVERED("Order has been delivered"),
    CANCELLED("Order has been cancelled"),
    FAILED("Order processing failed");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return this.name() + " - " + this.description;
    }
}
