package com.loopers.interfaces.api.order;

import com.loopers.application.order.OrderFacade;
import com.loopers.interfaces.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.loopers.support.error.HeaderConstants.X_USER_ID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderV1Controller implements OrderV1ApiSpec{
    private final OrderFacade orderFacade;

    @PostMapping
    @Override
    public ApiResponse<OrderV1Dto.OrderResponse> order(@RequestHeader(value = X_USER_ID) String userId, @RequestBody OrderV1Dto.Order order) {
        orderFacade.order(order.toEntity(userId));
        return ApiResponse.success(null);
    }
}
