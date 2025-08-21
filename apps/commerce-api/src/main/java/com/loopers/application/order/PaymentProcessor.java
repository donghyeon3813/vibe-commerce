package com.loopers.application.order;

import com.loopers.domain.order.OrderModel;
import com.loopers.domain.user.UserModel;


public interface PaymentProcessor {
    void process(OrderModel orderModel, OrderCommand.Order order, UserModel user);

    boolean check(OrderCommand.Order payment);
}
