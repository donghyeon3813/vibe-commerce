package com.loopers.support.callback;
public enum PaymentCallBack {
    PG_SIMULATOR("http://localhost:8080/api/v1/payment/callback");
    final String callbackUrl;

    PaymentCallBack(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }
}
