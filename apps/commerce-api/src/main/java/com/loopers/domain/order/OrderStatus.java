package com.loopers.domain.order;

public enum OrderStatus {
    CREATED,
    PAYMENT_PENDING,
    CANCELLED,
    PAID;

    public OrderStatus getOrderStatus() {
        return this;
    }
}
