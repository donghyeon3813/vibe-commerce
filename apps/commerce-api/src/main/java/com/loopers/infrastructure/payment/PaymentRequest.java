package com.loopers.infrastructure.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PaymentRequest {
    private String orderId;
    private String cardType;
    private String cardNo;
    private String amount;
    private String callbackUrl;
    public static PaymentRequest of(Long orderId, String cardType, String cardNo, String amount, String callbackUrl) {
        return new PaymentRequest(String.format("%06d", orderId), cardType, cardNo, amount, callbackUrl);
    }
}
