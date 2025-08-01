package com.loopers.application.order;

public class OrderInfo {
    public record OrderResponse(Long orderId) {
        public static OrderResponse of(Long orderId) {
            return new OrderResponse(orderId);
        }
    }
}
