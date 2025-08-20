package com.loopers.infrastructure.payment;

import com.loopers.application.order.OrderCommand;
import com.loopers.domain.order.OrderModel;
import com.loopers.domain.payment.PaymentGateway;
import com.loopers.support.callback.PaymentCallBack;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PgSimulator implements PaymentGateway {
    private final PgClient pgClient;

    @Override
    public String pay(OrderModel orderModel, OrderCommand.Order order) {
        try {
            PaymentRequest paymentRequest = PaymentRequest.of(
                    orderModel.getId(),
                    order.getCardType(),
                    order.getCardNo(),
                    orderModel.getAmount().toString(),
                    PaymentCallBack.PG_SIMULATOR.getCallbackUrl());
            PaymentResponse response = pgClient.pay("133515", paymentRequest);
            return response.getData().getTransactionKey();//dycj
        } catch (FeignException.BadRequest e) {
            throw new CoreException(ErrorType.BAD_REQUEST, e.getMessage());
        }

    }
}
