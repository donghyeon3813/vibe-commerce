package com.loopers.interfaces.api.payment;

import com.loopers.application.payment.PaymentCommand;
import com.loopers.application.payment.PaymentFacade;
import com.loopers.interfaces.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payment")
public class PaymentV1Controller implements PaymentV1ApiSpec{

    private final PaymentFacade paymentFacade;

    @PostMapping("/callback")
    @Override
    public void callback(@RequestBody PaymentV1Dto.PaymentCallbackRequest paymentCallbackRequest) {
        PaymentCommand.PaymentStatusUpdate paymentStatusUpdate = PaymentCommand.PaymentStatusUpdate.of(paymentCallbackRequest.getTransactionKey(), paymentCallbackRequest.getOrderId(), paymentCallbackRequest.getStatus());
        paymentFacade.updatePayment(paymentStatusUpdate);
    }
}
