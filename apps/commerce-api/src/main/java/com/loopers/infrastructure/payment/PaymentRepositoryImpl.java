package com.loopers.infrastructure.payment;

import com.loopers.domain.payment.Payment;
import com.loopers.domain.payment.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {
    private final PaymentJpaRepository paymentRepository;

    @Override
    public void save(Payment payment) {
        paymentRepository.save(payment);
    }
}
