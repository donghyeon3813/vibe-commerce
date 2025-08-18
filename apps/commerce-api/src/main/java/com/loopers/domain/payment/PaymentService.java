package com.loopers.domain.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;


    @Transactional
    public void createPayment(Long orderId) {
        Payment payment = Payment.create(orderId, PayType.POINT, null);
        paymentRepository.save(payment);
    }


}
