package com.loopers.infrastructure.order;

import com.loopers.domain.order.OrderModel;
import com.loopers.domain.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {
    private final OrderJpaRepository orderJpaRepository;

    @Override
    public OrderModel save(OrderModel orderModel) {
        return orderJpaRepository.save(orderModel);
    }
}
