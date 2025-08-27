package com.loopers.application.payment;

import com.loopers.application.order.OrderCommand;
import com.loopers.domain.order.OrderModel;
import com.loopers.domain.user.UserModel;


public interface PaymentProcessor {
    void process(OrderModel orderModel, OrderCommand.Order order, UserModel user);

    boolean check(OrderCommand.Order payment);
}
