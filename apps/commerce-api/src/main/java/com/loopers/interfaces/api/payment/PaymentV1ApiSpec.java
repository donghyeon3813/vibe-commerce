package com.loopers.interfaces.api.payment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Payment V1 API", description = "결제 API 입니다.")
public interface PaymentV1ApiSpec {
    @Operation(summary = "포인트 조회", description = "header 의 X-USER-ID를 통해 포인트 정보를 가져옵니다.")
    void callback(
            @Schema(name = "결제 데이터를 받아옵니다.")
            PaymentV1Dto.PaymentCallbackRequest paymentCallbackRequest);
}
