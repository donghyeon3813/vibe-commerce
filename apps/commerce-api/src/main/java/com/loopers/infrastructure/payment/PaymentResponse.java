package com.loopers.infrastructure.payment;

import lombok.Getter;
@Getter
public class PaymentResponse {

    private Meta meta;
    private Data data;

    @Getter
    public static class Meta {
        private String result;
    }

    @Getter
    public static class Data {
        private String transactionKey;
        private String status;
    }
}
