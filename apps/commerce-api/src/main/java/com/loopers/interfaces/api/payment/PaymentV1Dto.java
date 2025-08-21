package com.loopers.interfaces.api.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;

public class PaymentV1Dto {
    @Getter
    @NoArgsConstructor
    public static class PaymentCallbackRequest {
        private String transactionKey; // 거래 키
        private String orderId;        // 주문 번호
        private String cardType;       // 카드사 종류
        private String cardNo;         // 카드 번호 (마스킹된 형태)
        private Integer amount;        // 결제 금액
        private String status;         // SUCCESS, FAILED 등 상태
        private String reason;         // 결과 사유 (성공/실패 메세지)
    }
}
