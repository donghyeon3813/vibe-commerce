package com.loopers.domain.payment;

import com.loopers.application.order.OrderCommand;
import com.loopers.domain.order.OrderModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentGateway paymentGateway;



    public Payment createPayment(Long orderId, PayType payType) {
        Payment payment = Payment.create(orderId, payType, null);
        return paymentRepository.save(payment);
    }


    public String cardPay(OrderModel orderModel, OrderCommand.Order order) {
        return paymentGateway.pay(orderModel, order);

    }
}
