package com.loopers.domain.order;

import com.loopers.application.order.OrderCommand;
import com.loopers.domain.product.Product;
import com.loopers.infrastructure.product.ProductJpaRepository;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.Assert.assertThrows;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@SpringBootTest
public class OrderServiceTest {
    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductJpaRepository productJpaRepository;

    @DisplayName("상품의 가격을 계산할 때")
    @Nested
    class Calculate {
        @DisplayName("값이 비워져 있으면 BadRequest를 반환한다.")
        @Test
        void throwBadRequestException_WhenProductIsNull() {
            List<Product> productList = new ArrayList<>();
            Product product1 = productJpaRepository.save(Product.create(9999L, "상의", 1000, 5));  // name: 상의
            Product product2 = productJpaRepository.save(Product.create(9999L, "하의", 500, 5));  // name: 하의
            Product product3 = productJpaRepository.save(Product.create(9999L, "신발", 100, 5));   // name: 신발
            productList.add(null);

            List<OrderCommand.Order.OrderItem> orderItemList = new ArrayList<>();
            orderItemList.add(OrderCommand.Order.OrderItem.of(1L, 1));
            orderItemList.add(OrderCommand.Order.OrderItem.of(2L, 2));
            orderItemList.add(OrderCommand.Order.OrderItem.of(3L, 2));
            CoreException exception = assertThrows(CoreException.class, () -> orderService.calulateTotalAmount(orderItemList, productList, null));

            assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);

        }

        @DisplayName("누락된 항목이 없으면 값이 계산된다.")
        @Test
        void success() {
            List<Product> productList = new ArrayList<>();
            Product product1 = productJpaRepository.save(Product.create(9999L, "상의", 1000, 5));  // name: 상의
            Product product2 = productJpaRepository.save(Product.create(9999L, "하의", 500, 5));  // name: 하의
            Product product3 = productJpaRepository.save(Product.create(9999L, "신발", 100, 5));   // name: 신발
            productList.add(product1);
            productList.add(product2);
            productList.add(product3);
            List<OrderCommand.Order.OrderItem> orderItemList = new ArrayList<>();
            orderItemList.add(OrderCommand.Order.OrderItem.of(product1.getId(), 2));
            orderItemList.add(OrderCommand.Order.OrderItem.of(product2.getId(), 2));
            orderItemList.add(OrderCommand.Order.OrderItem.of(product3.getId(), 2));
            BigDecimal totalAmount = orderService.calulateTotalAmount(orderItemList, productList, null);

            assertThat(totalAmount.doubleValue()).isEqualTo(3200);

        }
    }

}
