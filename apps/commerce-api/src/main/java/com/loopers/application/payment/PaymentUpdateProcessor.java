package com.loopers.application.payment;

import com.loopers.domain.order.OrderModel;
import com.loopers.domain.payment.Payment;

public interface PaymentUpdateProcessor {
    void process(OrderModel order, Payment payment);

    boolean check(String status);
}
