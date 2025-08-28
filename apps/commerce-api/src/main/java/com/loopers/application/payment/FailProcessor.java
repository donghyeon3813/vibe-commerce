package com.loopers.application.payment;

import com.loopers.application.delivery.listener.DeliveryEvent;
import com.loopers.application.order.listener.OrderEvent;
import com.loopers.domain.payment.Payment;
import com.loopers.domain.product.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class FailProcessor implements PaymentUpdateProcessor{
    private final ProductService productService;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    @Override
    public void process(Payment payment) {
        log.info("Processing fail payment {}", payment);
        payment.fail();
        applicationEventPublisher.publishEvent(DeliveryEvent.PaymentResultEvent
                .of(payment.getOrderUid(), payment.getPayType(), payment.getPaymentStatus(), payment.getTransactionKey()));
    }

    @Override
    public boolean check(String status) {
        return "FAILED".equals(status);
    }
}
