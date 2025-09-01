package com.loopers.application.payment;

import com.loopers.application.order.OrderCommand;
import com.loopers.domain.order.OrderModel;
import com.loopers.domain.payment.PayType;
import com.loopers.domain.payment.Payment;
import com.loopers.domain.payment.PaymentService;
import com.loopers.domain.user.UserModel;
import com.loopers.support.error.CoreException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CardProcessor implements PaymentProcessor {
    private final PaymentService paymentService;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, noRollbackFor = CoreException.class)
    public void process(OrderModel orderModel, OrderCommand.Order order, UserModel user) {
        Payment payment = paymentService.createPayment(orderModel.getId(), PayType.CARD);
        try {
            String transactionKey = paymentService.cardPay(orderModel, order);
            payment.requestSuccess(transactionKey);
        } catch (CoreException e){
            payment.fail();
            orderModel.changeStatusToCanceled();
            throw e;
        }
    }

    @Override
    public boolean check(OrderCommand.Order order) {
        return order.getPayment() == OrderCommand.Order.Payment.CARD;
    }
}
