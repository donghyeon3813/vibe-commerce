package com.loopers.domain.order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    OrderModel save(OrderModel orderModel);

    List<OrderModel> findByUserId(Long id);

    Optional<OrderModel> findByIdAndUserUid(Long userId, Long orderId);
}
