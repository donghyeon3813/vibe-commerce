package com.loopers.application.payment;

import com.loopers.application.delivery.listener.DeliveryEvent;
import com.loopers.domain.payment.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class SuccessProcessor implements PaymentUpdateProcessor {
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    @Override
    public void process(Payment payment) {
        log.info("Processing payment {}", payment);
        payment.success();
        applicationEventPublisher.publishEvent(DeliveryEvent.PaymentResultEvent
                .of(payment.getOrderUid(), payment.getPayType(), payment.getPaymentStatus(), payment.getTransactionKey()));
    }

    @Override
    public boolean check(String status) {
        return "SUCCESS".equals(status);
    }
}
