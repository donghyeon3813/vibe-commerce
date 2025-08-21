package com.loopers.application.payment;

import lombok.Getter;

public class PaymentCommand {
    @Getter
    public static class PaymentStatusUpdate{
        private String transactionKey; // 거래 키
        private String orderId;        // 주문 번호
        private String status;         // SUCCESS, FAILED 등 상태
        public static PaymentStatusUpdate of(String transactionKey, String orderId, String status){
            return new PaymentStatusUpdate(transactionKey, orderId, status);
        }

        private PaymentStatusUpdate(String transactionKey, String orderId, String status) {
            this.transactionKey = transactionKey;
            this.orderId = orderId;
            this.status = status;
        }
    }
}
