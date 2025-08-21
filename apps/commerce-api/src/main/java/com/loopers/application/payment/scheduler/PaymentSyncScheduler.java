package com.loopers.application.payment.scheduler;

import com.loopers.application.payment.PaymentStrategyFactory;
import com.loopers.domain.order.OrderModel;
import com.loopers.domain.order.OrderService;
import com.loopers.domain.payment.Payment;
import com.loopers.domain.payment.PaymentService;
import com.loopers.domain.payment.PaymentStatus;
import com.loopers.infrastructure.payment.PaymentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentSyncScheduler {
    private final PaymentService paymentService;
    private final OrderService orderService;
    private final PaymentStrategyFactory paymentStrategyFactory;

    @Scheduled(fixedRate = 1000 * 60 * 5)
    @Transactional
    void syncPayments() {
        log.info("Starting sync payments");
        List<Payment> pendingList = paymentService.getPendingList();
        if (pendingList.isEmpty()) {
            log.info("No pending payments to sync");
            return;
        }

        for (Payment payment : pendingList) {
            PaymentResponse pgPayment;
            try {
                pgPayment = paymentService.getPgPayment(payment.getTransactionKey());
                if (pgPayment == null) {
                    continue;
                }
                Optional<OrderModel> orderModel = orderService.getOrder(payment.getOrderUid());
                if (orderModel.isEmpty()) {
                    continue;
                }
                paymentStrategyFactory.getUpdateProcessor(pgPayment.getData().getStatus()).process(orderModel.get(), payment);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    @Scheduled(fixedRate = 1000 * 60 * 30)
    @Transactional
    void oldPaymentsFailed() {
        log.info("Starting old payments failed");
        ZonedDateTime thirtyMinutesAgo = ZonedDateTime.now().minusMinutes(30);
        List<Payment> pendingPayments = paymentService.findByPaymentStatusAndCreatedAtBefore(
                PaymentStatus.PENDING,
                thirtyMinutesAgo
        );
        if (pendingPayments.isEmpty()) {
            log.info("No old payments to sync");
            return;
        }

        for (Payment payment : pendingPayments) {
            Optional<OrderModel> orderModel = orderService.getOrder(payment.getOrderUid());
            if (orderModel.isEmpty()) {
                continue;
            }
            paymentStrategyFactory.getUpdateProcessor("FAILED").process(orderModel.get(), payment);
        }
    }

}
