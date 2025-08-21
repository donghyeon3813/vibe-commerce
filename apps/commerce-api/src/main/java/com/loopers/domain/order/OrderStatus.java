package com.loopers.domain.order;

public enum OrderStatus {
    CREATED,
    PAYMENT_PENDING,
    CANCELLED,
    FAILED,
    PAID;

    public OrderStatus getOrderStatus() {
        return this;
    }
}
