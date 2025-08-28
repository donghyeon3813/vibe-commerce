package com.loopers.application.payment;

import com.loopers.domain.payment.Payment;

public interface PaymentUpdateProcessor {
    void process(Payment payment);

    boolean check(String status);
}
