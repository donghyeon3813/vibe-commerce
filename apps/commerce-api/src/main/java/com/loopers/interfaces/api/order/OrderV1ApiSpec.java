package com.loopers.interfaces.api.order;

import com.loopers.interfaces.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

public interface OrderV1ApiSpec {
    @Tag(name = "Order V1 API", description = "주문 및 결제 요청 API 입니다.")
    @Operation(
            summary = "주문 및 결제 요청",
            description = "입력된 상품들의 주문 및 결제를 처리합니다."
    )
    ApiResponse<OrderV1Dto.OrderResponse> order(String userId, OrderV1Dto.Order order);

}
