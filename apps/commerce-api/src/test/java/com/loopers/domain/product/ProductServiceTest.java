package com.loopers.domain.product;

import com.loopers.infrastructure.product.ProductJpaRepository;
import com.loopers.infrastructure.product.ProductQueryDslImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @MockitoSpyBean
    private ProductJpaRepository productJpaRepository;
    @MockitoSpyBean
    private ProductQueryDslImpl productQueryDsl;

    @DisplayName("상품 목록을 조회할 때")
    @Nested
    class Infos {
        @DisplayName("productJpaRepository 를 호출하는지 확인한다.")
        @Test
        void testProductJpaRepositoryIsCalled(){
            Long brandId = 1L;
            Pageable pageable = PageRequest.of(0, 10);

            productService.getProductList(brandId, pageable);

            verify(productQueryDsl, times(1)).findAllByPageable(brandId, pageable);
        }
    }

    @DisplayName("상품 상세정보를 조회할 때")
    @Nested
    class Info {
        @DisplayName("productJpaRepository 를 호출하는지 확인한다.")
        @Test
        void testProductJpaRepositoryIsCalled(){
            Product savedProduct = productJpaRepository.save(Product.create(1L, "과일", 1000, 5));

            productService.getProductInfo(savedProduct.getId());
            verify(productJpaRepository, times(1)).findById(savedProduct.getId());
        }
    }
}
