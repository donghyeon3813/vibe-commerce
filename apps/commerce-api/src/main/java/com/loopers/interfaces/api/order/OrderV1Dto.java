package com.loopers.interfaces.api.order;

import com.loopers.application.order.OrderCommand;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class OrderV1Dto {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItem {
        private Long productId;
        private int quantity;
        public com.loopers.application.order.OrderCommand.Order.OrderItem toEntity() {
            return new com.loopers.application.order.OrderCommand.Order.OrderItem(productId, quantity);
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Order {
        private List<OrderItem> items;
        private String address;
        private String phone;
        private String receiverName;
        private Long couponId;
        private String cardType;
        private String cardNo;

        public OrderCommand.Order toEntity(String userId) {
            return OrderCommand.Order.ofCard(
                    items.stream()
                            .map(OrderItem::toEntity)
                            .toList(),
                    userId,
                    address,
                    phone,
                    receiverName,
                    couponId,
                    cardType,
                    cardNo
            );
        }
    }

    public class OrderResponse {
    }
}
