package com.loopers.application.payment;

import com.loopers.domain.order.OrderModel;
import com.loopers.domain.payment.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class SuccessProcessor implements PaymentUpdateProcessor {

    @Transactional
    @Override
    public void process(OrderModel order, Payment payment) {
        log.info("Processing payment {}", payment);
        order.changeStatusToPaid();
        payment.success();
    }

    @Override
    public boolean check(String status) {
        return "SUCCESS".equals(status);
    }
}
