package com.loopers.domain.order;

import com.loopers.application.order.OrderCommand;
import com.loopers.domain.product.Product;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public int calulateTotalAmount(List<OrderCommand.Order.OrderItem> items, List<Product> productList) {
        Map<Long, Product> productMap = productList.stream()
                .peek(product -> {
                    if (product == null) {
                        throw new CoreException(ErrorType.BAD_REQUEST, "상품 목록에 null 값이 포함되어 있습니다.");
                    }
                })
                .collect(Collectors.toMap(Product::getId, p -> p));

        int totalAmount = 0;
        for (OrderCommand.Order.OrderItem item : items) {
            Product product = productMap.get(item.getProductId());
            totalAmount += product.getAmount() * item.getQuantity();
        }
        return totalAmount;

    }

    @Transactional
    public OrderModel create(Long userId, List<OrderCommand.Order.OrderItem> items, int totalAmount, String phone, String receiverName, String address) {
        OrderModel orderModel = OrderModel.create(userId, totalAmount, Address.of(address, phone, receiverName));
        for (OrderCommand.Order.OrderItem item : items) {
            orderModel.addOrderItem(OrderItem.create(item.getProductId(), item.getQuantity()));
        }
        return orderRepository.save(orderModel);
    }
}
