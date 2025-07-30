package com.loopers.domain.product;

import com.loopers.infrastructure.product.ProductJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
public class ProductServiceIntegrationTest {
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductJpaRepository productJpaRepository;

    @DisplayName("상품을 조회할 때")
    @Nested
    class Info {
        @DisplayName("일치하는 값이 없으면 빈항목을 반환한다.")
        @Test
        void returnsEmptyProduct_whenProductNotExist() {
            Long productId = 9999L;

            Optional<Product> productInfo = productService.getProductInfo(productId);

            assertThat(productInfo).isEmpty();

        }

        @DisplayName("일치하는 값이 있으면 product 를 반환한다.")
        @Test
        void returnsEmptyProduct_whenProductExist() {
            Product savedProduct = productJpaRepository.save(Product.create(1L, "과일", 1000, 5));

            Optional<Product> productInfo = productService.getProductInfo(savedProduct.getId());

            assertAll(
                    () -> assertThat(productInfo).isPresent(),
                    () -> assertThat(productInfo.get().getId()).isEqualTo(savedProduct.getId()),
                    () -> assertThat(productInfo.get().getName()).isEqualTo(savedProduct.getName()),
                    () -> assertThat(productInfo.get().getBrandUid()).isEqualTo(savedProduct.getBrandUid()),
                    () -> assertThat(productInfo.get().getAmount()).isEqualTo(savedProduct.getAmount()),
                    () -> assertThat(productInfo.get().getQuantity()).isEqualTo(savedProduct.getQuantity())
            );

        }
    }
}
