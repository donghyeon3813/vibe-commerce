package com.loopers.domain.order;

import com.loopers.application.order.OrderCommand;
import com.loopers.domain.coupon.Coupon;
import com.loopers.domain.product.Product;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public BigDecimal calulateTotalAmount(List<OrderCommand.Order.OrderItem> items, List<Product> productList, Coupon coupon) {
        Map<Long, Product> productMap = productList.stream()
                .peek(product -> {
                    if (product == null) {
                        throw new CoreException(ErrorType.BAD_REQUEST, "상품 목록에 null 값이 포함되어 있습니다.");
                    }
                })
                .collect(Collectors.toMap(Product::getId, p -> p));

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (OrderCommand.Order.OrderItem item : items) {
            Product product = productMap.get(item.getProductId());
            totalAmount = totalAmount.add(BigDecimal.valueOf((long) product.getAmount() * item.getQuantity()));
        }
        if (coupon != null) {
            totalAmount = coupon.calculateDiscount(totalAmount);
        }
        if (totalAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new CoreException(ErrorType.BAD_REQUEST, "총 금액은 0보다 커야 합니다.");
        }
        return totalAmount;

    }

    @Transactional
    public OrderModel create(Long userId, List<OrderCommand.Order.OrderItem> items, BigDecimal totalAmount, String phone, String receiverName, String address, Long couponId) {
        OrderModel orderModel = OrderModel.create(userId, totalAmount, Address.of(address, phone, receiverName), couponId);
        for (OrderCommand.Order.OrderItem item : items) {
            orderModel.addOrderItem(OrderItem.create(item.getProductId(), item.getQuantity()));
        }
        return orderRepository.save(orderModel);
    }

    public List<OrderModel> getOrders(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    public Optional<OrderModel> getOrder(Long orderId, Long userId) {
        return orderRepository.findByIdAndUserUid(userId, orderId);
    }
    public Optional<OrderModel> getOrder(Long orderId) {
        return orderRepository.findById(orderId);
    }
}
