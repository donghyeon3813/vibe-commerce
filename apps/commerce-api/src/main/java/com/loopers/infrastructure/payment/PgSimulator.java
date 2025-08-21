package com.loopers.infrastructure.payment;

import com.loopers.application.order.OrderCommand;
import com.loopers.domain.order.OrderModel;
import com.loopers.domain.payment.PaymentGateway;
import com.loopers.support.callback.PaymentCallBack;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PgSimulator implements PaymentGateway {
    private final PgClient pgClient;

    @CircuitBreaker(name = "pgCircuit")
    @Retry(name = "pgRetry", fallbackMethod = "payFallback")
    @Override
    public String pay(OrderModel orderModel, OrderCommand.Order order) {
        log.info("PgSimulator.pay({}, {})", orderModel, order);
        try {
            PaymentRequest paymentRequest = PaymentRequest.of(
                    orderModel.getId(),
                    order.getCardType(),
                    order.getCardNo(),
                    orderModel.getAmount().toString(),
                    PaymentCallBack.PG_SIMULATOR.getCallbackUrl());
            PaymentResponse response = pgClient.pay("133515", paymentRequest);
            return response.getData().getTransactionKey();
        } catch (FeignException.BadRequest e) {
            throw new CoreException(ErrorType.BAD_REQUEST, "pg 오류가 발생했습니다.");
        }
    }

    @Override
    public PaymentResponse get(String transactionKey) {
        PaymentResponse response = pgClient.get(transactionKey, "133515");
        return response;
    }

    @Override
    public PaymentResponse getByOrderUid(Long orderUid) {
        return pgClient.getPayments("133515", String.format("%06d", orderUid));
    }

    public String payFallback(OrderModel orderModel, OrderCommand.Order order, Throwable t) {
        log.error("결제 재시도 실패: {}", t.getMessage(), t);

        throw new CoreException(ErrorType.INTERNAL_ERROR, "pg 오류가 발생했습니다.");
    }
}
