package com.loopers.infrastructure.order;

import com.loopers.domain.order.OrderModel;
import com.loopers.domain.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {
    private final OrderJpaRepository orderJpaRepository;

    @Override
    public OrderModel save(OrderModel orderModel) {
        return orderJpaRepository.save(orderModel);
    }

    @Override
    public List<OrderModel> findByUserId(Long id) {
        return orderJpaRepository.findAllByUserUid(id);
    }

    @Override
    public Optional<OrderModel> findByIdAndUserUid(Long userId, Long orderId) {
        return orderJpaRepository.findByIdAndUserUid(orderId, userId);
    }
}
