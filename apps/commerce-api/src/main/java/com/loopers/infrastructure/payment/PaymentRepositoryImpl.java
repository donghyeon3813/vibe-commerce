package com.loopers.infrastructure.payment;

import com.loopers.domain.payment.Payment;
import com.loopers.domain.payment.PaymentRepository;
import com.loopers.domain.payment.PaymentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {
    private final PaymentJpaRepository paymentRepository;

    @Override
    public Payment save(Payment payment) {
        return paymentRepository.save(payment);
    }

    @Override
    public Optional<Payment> findByTransactionKey(String transactionKey) {
        return paymentRepository.findByTransactionKey(transactionKey);
    }

    @Override
    public List<Payment> findAllByPaymentStatus(PaymentStatus paymentStatus) {
        return paymentRepository.findAllByPaymentStatus(paymentStatus);
    }

    @Override
    public List<Payment> findByPaymentStatusAndCreatedAtBefore(PaymentStatus paymentStatus, ZonedDateTime thirtyMinutesAgo) {
        return paymentRepository.findByPaymentStatusAndCreatedAtBefore(paymentStatus, thirtyMinutesAgo);
    }
}
