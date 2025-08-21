package com.loopers.application.payment;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PaymentStrategyFactory {

    private final List<PaymentUpdateProcessor> paymentUpdateProcessors;

    public PaymentUpdateProcessor getUpdateProcessor(String status) {
        return paymentUpdateProcessors.stream()
                .filter(p -> p.check(status))
                .findFirst()
                .orElseThrow( () -> new CoreException(ErrorType.BAD_REQUEST, "지원하지 않는 결제타입입니다."));
    }
}
