package com.loopers.application.delivery.listener;


import com.loopers.domain.order.Address;
import com.loopers.domain.order.OrderItem;
import com.loopers.domain.order.OrderStatus;
import com.loopers.domain.payment.PayType;
import com.loopers.domain.payment.PaymentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

public class DeliveryEvent {
    @Getter
    public static class OrderSuccessEvent {
        private Long userUid;

        private Address address;

        private OrderStatus orderStatus;

        private BigDecimal amount;

        private Long couponUid;

        private List<OrderItem> items;

        private OrderSuccessEvent(Long userUid, Address address, OrderStatus orderStatus, BigDecimal amount, Long couponUid, List<OrderItem> items) {
            this.userUid = userUid;
            this.address = address;
            this.orderStatus = orderStatus;
            this.amount = amount;
            this.couponUid = couponUid;
            this.items = items;
        }
        public static OrderSuccessEvent of(Long userUid, Address address, OrderStatus orderStatus, BigDecimal amount, Long couponUid, List<OrderItem> items) {
            return new OrderSuccessEvent(userUid, address, orderStatus, amount, couponUid, items);
        }
    }
    @Getter
    public static class PaymentSuccessEvent {
        private Long orderUid;
        private PayType payType;
        private PaymentStatus paymentStatus;
        private String transactionKey;

        private PaymentSuccessEvent(Long orderUid, PayType payType, PaymentStatus paymentStatus, String transactionKey) {
            this.orderUid = orderUid;
            this.payType = payType;
            this.paymentStatus = paymentStatus;
            this.transactionKey = transactionKey;
        }
        public static PaymentSuccessEvent of(Long orderUid, PayType payType, PaymentStatus paymentStatus, String transactionKey) {
            return new PaymentSuccessEvent(orderUid, payType, paymentStatus, transactionKey);
        }
    }

}
