package com.loopers.domain.payment;

import com.loopers.application.order.OrderCommand;
import com.loopers.domain.order.OrderModel;
import com.loopers.infrastructure.payment.PaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentGateway paymentGateway;



    public Payment createPayment(Long orderId, PayType payType) {
        Payment payment = Payment.create(orderId, payType, null);
        return paymentRepository.save(payment);
    }


    public String cardPay(OrderModel orderModel, OrderCommand.Order order) {
        return paymentGateway.pay(orderModel, order);

    }

    public Optional<Payment> getPayment(String transactionKey) {
        return paymentRepository.findByTransactionKey(transactionKey);
    }

    public PaymentResponse getPgPaymentByTransaction(String transactionKey) {
        return paymentGateway.get(transactionKey);
    }

    public List<Payment> getPendingList() {
        return paymentRepository.findAllByPaymentStatus(PaymentStatus.PENDING);
    }

    public List<Payment> findByPaymentStatusAndCreatedAtBefore(PaymentStatus paymentStatus, ZonedDateTime thirtyMinutesAgo) {
        return paymentRepository.findByPaymentStatusAndCreatedAtBefore(paymentStatus, thirtyMinutesAgo);
    }

    public PaymentResponse getPgPaymentByOrderId(Long orderUid) {
        return paymentGateway.getByOrderUid(orderUid);
    }
}
