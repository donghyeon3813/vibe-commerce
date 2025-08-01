package com.loopers.application.order;

import lombok.Getter;

import java.util.List;

public class OrderCommand {
    @Getter
    public static class Order {
        private final List<OrderItem> items;
        private final String userId;
        private final String address;
        private final String phone;
        private final String receiverName;

        public Order(List<OrderItem> items, String userId, String address, String phone, String receiverName) {
            this.items = items;
            this.userId = userId;
            this.address = address;
            this.phone = phone;
            this.receiverName = receiverName;
        }

        public static Order of(List<OrderItem> items, String userId, String address, String phone, String receiverName) {
            return new Order(items, userId, address, phone, receiverName);
        }

        @Getter
        public static class OrderItem {
            private final Long productId;
            private final int quantity;

            public OrderItem(Long productId, int quantity) {
                this.productId = productId;
                this.quantity = quantity;
            }

            public static OrderItem of(Long productId, int quantity) {
                return new OrderItem(productId, quantity);
            }

        }

    }
    public record GetOrders(String userId) {
        public static GetOrders of(String userId) {
            return new GetOrders(userId);
        }
        public GetOrders(String userId) {
            this.userId = userId;
        }

    }

    public record GetOrder(String userId, Long orderId) {
        public GetOrder(String userId, Long orderId) {
            this.userId = userId;
            this.orderId = orderId;
        }
        public static GetOrder of(String userId, Long orderId) {
            return new GetOrder(userId, orderId);
        }
    }
}
