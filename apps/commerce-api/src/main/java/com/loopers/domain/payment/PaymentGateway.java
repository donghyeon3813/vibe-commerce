package com.loopers.domain.payment;

import com.loopers.application.order.OrderCommand;
import com.loopers.domain.order.OrderModel;
import com.loopers.infrastructure.payment.PaymentResponse;

public interface PaymentGateway {
    String pay(OrderModel orderModel, OrderCommand.Order order);

    PaymentResponse get(String transactionKey);

    PaymentResponse getByOrderUid(Long orderUid);
}
