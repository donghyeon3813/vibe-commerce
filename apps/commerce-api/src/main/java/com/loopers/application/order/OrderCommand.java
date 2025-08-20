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
        private final Long couponId;
        private final String cardType;
        private final String cardNo;
        private final Payment payment;

        public Order(List<OrderItem> items, String userId, String address, String phone, String receiverName, Long couponId) {
            this.items = items;
            this.userId = userId;
            this.address = address;
            this.phone = phone;
            this.receiverName = receiverName;
            this.couponId = couponId;
            this.cardType = null;
            this.cardNo = null;
            this.payment = Payment.POINT;

        }

        public Order(List<OrderItem> items, String userId, String address, String phone, String receiverName, Long couponId, String cardType, String cardNo, Payment payment) {
            this.items = items;
            this.userId = userId;
            this.address = address;
            this.phone = phone;
            this.receiverName = receiverName;
            this.couponId = couponId;
            this.cardType = cardType;
            this.cardNo = cardNo;
            this.payment = payment;
        }

        public static Order of(List<OrderItem> items, String userId, String address, String phone, String receiverName, Long couponId) {
            return new Order(items, userId, address, phone, receiverName, couponId, null, null, Payment.POINT);
        }

        public static Order ofCard(List<OrderItem> items, String userId, String address, String phone, String receiverName,
                                   Long couponId, String cardType, String cardNo) {
            return new Order(items, userId, address, phone, receiverName, couponId, cardType, cardNo, Payment.CARD);
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
        enum Payment{
            POINT,CARD
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
