package com.loopers.domain.payment;

import com.loopers.application.order.OrderCommand;
import com.loopers.domain.order.OrderModel;

public interface PaymentGateway {
    String pay(OrderModel orderModel, OrderCommand.Order order);
}
