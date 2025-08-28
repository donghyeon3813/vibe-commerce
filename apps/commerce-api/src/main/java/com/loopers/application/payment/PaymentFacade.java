package com.loopers.application.payment;

import com.loopers.application.order.listener.OrderEvent;
import com.loopers.domain.order.OrderModel;
import com.loopers.domain.order.OrderService;
import com.loopers.domain.payment.Payment;
import com.loopers.domain.payment.PaymentService;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PaymentFacade {
    private final OrderService orderService;
    private final PaymentService paymentService;
    private final PaymentStrategyFactory paymentStrategyFactory;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void updatePayment(PaymentCommand.PaymentStatusUpdate command) {

        Payment payment = paymentService.getPayment(command.getTransactionKey())
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "해당 하는 결제 정보가 없습니다."));
        eventPublisher.publishEvent(OrderEvent.StatusUpdateEvent.create(Long.valueOf(command.getOrderId()), command.getStatus()));

        paymentStrategyFactory.getUpdateProcessor(command.getStatus()).process(payment);


    }

}
