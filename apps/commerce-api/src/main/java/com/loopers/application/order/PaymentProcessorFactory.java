package com.loopers.application.order;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PaymentProcessorFactory {
    private final List<PaymentProcessor> paymentProcessors;

    public PaymentProcessor getPaymentProcessor(OrderCommand.Order order) {
        return paymentProcessors.stream()
                .filter(p -> p.check(order))
                .findFirst()
                .orElseThrow( () -> new CoreException(ErrorType.BAD_REQUEST, "지원하지 않는 결제타입입니다."));
    }
}
