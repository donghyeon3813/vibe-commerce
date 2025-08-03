package com.loopers.application.order;



import com.loopers.domain.order.OrderModel;

import java.util.List;

public class OrderInfo {
    public record OrderResponse(Long orderId) {
        public static OrderResponse of(Long orderId) {
            return new OrderResponse(orderId);
        }
    }
    public record OrderDataList(List<OrderModel> orders){
        public static OrderDataList of(List<OrderModel> orders) {
            return new OrderDataList(orders);
        }
    }
    public record OrderData(OrderModel order) {
        public static OrderData of(OrderModel order) {
            return new OrderData(order);
        }
    }
}
