package com.loopers.domain.payment;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository {
    Payment save(Payment payment);

    Optional<Payment> findByTransactionKey(String transactionKey);

    List<Payment> findAllByPaymentStatus(PaymentStatus paymentStatus);

    List<Payment> findByPaymentStatusAndCreatedAtBefore(PaymentStatus paymentStatus, ZonedDateTime thirtyMinutesAgo);
}
