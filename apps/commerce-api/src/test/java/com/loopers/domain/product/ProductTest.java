package com.loopers.domain.product;

import com.loopers.domain.brand.Brand;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

public class ProductTest {

    @DisplayName("product 상세 정보를 조합할 때")
    @Nested
    class Info {
        @DisplayName("brand 가 누락되면 NotFound 가 발생한다.")
        @Test
        void throwsNotFound_whenBrandIsMissing(){
            ProductDetailComposer composer = new ProductDetailComposer();
            Brand brand = Brand.create("무신사");

            CoreException exception = assertThrows(CoreException.class, () -> composer.compose(null, brand, 5));

            assertThat(exception.getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
        }

        @DisplayName("product 가 누락되면 NotFound 가 발생한다.")
        @Test
        void throwsNotFound_whenProductIsMissing(){
            ProductDetailComposer composer = new ProductDetailComposer();
            Product product = Product.create(1L, "과일", 1000, 5);

            CoreException exception = assertThrows(CoreException.class, () -> composer.compose(product, null, 5));

            assertThat(exception.getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
        }

        @DisplayName("product 와 bland 정보가 존재하면 ProductData 를 반환한다")
        @Test
        void returnsProductDetailData_whenProductAndBrandExist(){
            Product product = Product.create(1L, "과일", 1000, 5);
            Brand brand = Brand.create("무신사");
            ProductDetailComposer composer = new ProductDetailComposer();
            int likeCount = 5;

            ProductData productData = composer.compose(product, brand, likeCount);

            assertThat(productData).isNotNull();
            assertThat(productData.productName()).isEqualTo(product.getName());
            assertThat(productData.amount()).isEqualTo(product.getAmount());
            assertThat(productData.quantity()).isEqualTo(product.getQuantity());
            assertThat(productData.brandName()).isEqualTo(brand.getName());
            assertThat(productData.likeCount()).isEqualTo(likeCount);

        }
    }

}
