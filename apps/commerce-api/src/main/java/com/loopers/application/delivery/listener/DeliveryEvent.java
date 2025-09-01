package com.loopers.application.delivery.listener;


import com.loopers.domain.order.Address;
import com.loopers.domain.order.OrderItem;
import com.loopers.domain.order.OrderStatus;
import com.loopers.domain.payment.PayType;
import com.loopers.domain.payment.PaymentStatus;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

public class DeliveryEvent {
    @Getter
    public static class OrderResultEvent {
        private Long userUid;

        private Address address;

        private OrderStatus orderStatus;

        private BigDecimal amount;

        private Long couponUid;

        private List<OrderItem> items;

        private OrderResultEvent(Long userUid, Address address, OrderStatus orderStatus, BigDecimal amount, Long couponUid, List<OrderItem> items) {
            this.userUid = userUid;
            this.address = address;
            this.orderStatus = orderStatus;
            this.amount = amount;
            this.couponUid = couponUid;
            this.items = items;
        }
        public static OrderResultEvent of(Long userUid, Address address, OrderStatus orderStatus, BigDecimal amount, Long couponUid, List<OrderItem> items) {
            return new OrderResultEvent(userUid, address, orderStatus, amount, couponUid, items);
        }
    }
    @Getter
    public static class PaymentResultEvent {
        private Long orderUid;
        private PayType payType;
        private PaymentStatus paymentStatus;
        private String transactionKey;

        private PaymentResultEvent(Long orderUid, PayType payType, PaymentStatus paymentStatus, String transactionKey) {
            this.orderUid = orderUid;
            this.payType = payType;
            this.paymentStatus = paymentStatus;
            this.transactionKey = transactionKey;
        }
        public static PaymentResultEvent of(Long orderUid, PayType payType, PaymentStatus paymentStatus, String transactionKey) {
            return new PaymentResultEvent(orderUid, payType, paymentStatus, transactionKey);
        }
    }

}
