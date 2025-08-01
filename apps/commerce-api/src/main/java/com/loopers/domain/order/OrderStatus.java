package com.loopers.domain.order;

public enum OrderStatus {
    CREATED,
    PAYMENT_PENDING,
    PAID;

    public OrderStatus getOrderStatus() {
        return this;
    }
}
