package com.loopers.application;

import com.loopers.application.product.ProductCommand;
import com.loopers.application.product.ProductFacade;
import com.loopers.domain.product.Product;
import com.loopers.infrastructure.product.ProductJpaRepository;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class ProductFacadeIntegrationTest {
    @Autowired
    private ProductFacade productFacade;
    @Autowired
    private DatabaseCleanUp databaseCleanUp;
    @Autowired
    private ProductJpaRepository productJpaRepository;


    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

//    @DisplayName("상품 목록을 조회할 때")
//    @Nested
//    class InfoList {
//        @DisplayName("상품이 없으면 빈 목록을 반환한다.")
//        @Test
//        void returnsEmpty_whenUserExistsAndChargesSuccessfully() {
//            ProductCommand.ListInfoRequest getList = ProductCommand.ListInfoRequest.of(
//                    1L,
//                    ProductCommand.ListInfoRequest.Sort.LATEST,
//                    0,
//                    20
//            );
//            productFacade.getProductList(getList);
//
//        }
//    }

    @DisplayName("상품 상세 정보를 조회할 때")
    @Nested
    class Info {
        @DisplayName("상품이 없으면 404를 반환한다.")
        @Test
        void returnsNotFound_whenProductDoesNotExist() {
            ProductCommand.DetailInfoRequest getInfo = ProductCommand.DetailInfoRequest.of(9999L);
            Optional<Product> product = productJpaRepository.findById(9999L);
            assertThat(product).isEmpty();

            CoreException result = assertThrows(CoreException.class, () -> productFacade.getProductDetailInfo(getInfo));

            assertThat(result.getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
        }

        @DisplayName("상품의 브랜드 정보가 없으면 404를 반환한다.")
        @Test
        void returnsNotFound_whenBrandDoesNotExist() {
            Product product = Product.create(9999L, "과일", 1000, 5);
            Product savedProduct = productJpaRepository.save(product);

            ProductCommand.DetailInfoRequest getInfo = ProductCommand.DetailInfoRequest.of(savedProduct.getId());
            CoreException result = assertThrows(CoreException.class, () -> productFacade.getProductDetailInfo(getInfo));

            assertThat(result.getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
        }
    }
}
