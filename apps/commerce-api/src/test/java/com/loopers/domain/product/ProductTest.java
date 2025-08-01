package com.loopers.domain.product;

import com.loopers.domain.brand.Brand;
import com.loopers.domain.like.Like;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.reflect.Field;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

public class ProductTest {

    @DisplayName("product 상세 정보를 조합할 때")
    @Nested
    class Info {
        @DisplayName("brand 가 누락되면 NotFound 가 발생한다.")
        @Test
        void throwsNotFound_whenBrandIsMissing() {
            ProductDetailComposer composer = new ProductDetailComposer();
            Brand brand = Brand.create("무신사");

            CoreException exception = assertThrows(CoreException.class, () -> composer.compose(null, brand, 5));

            assertThat(exception.getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
        }

        @DisplayName("product 가 누락되면 NotFound 가 발생한다.")
        @Test
        void throwsNotFound_whenProductIsMissing() {
            ProductDetailComposer composer = new ProductDetailComposer();
            Product product = Product.create(1L, "과일", 1000, 5);

            CoreException exception = assertThrows(CoreException.class, () -> composer.compose(product, null, 5));

            assertThat(exception.getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
        }

        @DisplayName("product 와 bland 정보가 존재하면 ProductData 를 반환한다")
        @Test
        void returnsProductDetailData_whenProductAndBrandExist() {
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

    @DisplayName("내가 좋아요한 상품을 조회할때")
    @Nested
    class LikeWithProductData {

        @DisplayName("상품이 누락되면 제외된 항목을 반환한다.")
        @Test
        void returnsOnlyExistingProductsFromLikes() {

            Brand brand = Brand.create("나이키");
            setId(brand, "id", 9999L);
            Product product1 = Product.create(brand.getId(), "에어포스", 10000, 2);
            setId(product1, "id", 8000L);

            Like like1 = Like.create(9999L, 8000L);
            Like like2 = Like.create(9999L, 9999L);

            List<Product> products = List.of(product1);
            List<Brand> brands = List.of(brand);
            List<Like> likes = List.of(like1, like2);
            ProductDetailComposer composer = new ProductDetailComposer();

            List<ProductData> result = composer.composeList(products, brands, likes);

            assertThat(result).hasSize(1);
            assertThat(result.get(0).productName()).isEqualTo("에어포스");
            assertThat(result.get(0).brandName()).isEqualTo("나이키");
            assertThat(result.get(0).likeCount()).isEqualTo(1);
        }

        @DisplayName("브랜드 누락되면 제외된 항목을 반환한다.")
        @Test
        void returnProductList_excludesProducts_whenBrandIsMissing() {

            Brand brand = Brand.create("나이키");
            setId(brand, "id", 9999L);
            Product product1 = Product.create(brand.getId(), "에어포스1", 10000, 2);
            setId(product1, "id", 8000L);
            Product product2 = Product.create(8000L, "에어포스", 10000, 2);
            setId(product1, "id", 8000L);

            Like like1 = Like.create(9999L, product1.getId());
            Like like2 = Like.create(9999L, product2.getId());

            List<Product> products = List.of(product1);
            List<Brand> brands = List.of(brand);
            List<Like> likes = List.of(like1, like2);
            ProductDetailComposer composer = new ProductDetailComposer();

            List<ProductData> result = composer.composeList(products, brands, likes);

            assertThat(result).hasSize(1);
            assertThat(result.get(0).productName()).isEqualTo("에어포스1");
            assertThat(result.get(0).brandName()).isEqualTo("나이키");
            assertThat(result.get(0).likeCount()).isEqualTo(1);
        }

        private void setId(Object target, String fieldName, Object value) {
            try {
                Field field = getField(target.getClass(), fieldName);
                field.setAccessible(true);
                field.set(target, value);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        private Field getField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
            while (clazz != null) {
                try {
                    return clazz.getDeclaredField(fieldName);
                } catch (NoSuchFieldException e) {
                    clazz = clazz.getSuperclass(); // 부모 클래스로 탐색
                }
            }
            throw new NoSuchFieldException("Field '" + fieldName + "' not found in class hierarchy");
        }
    }

    @DisplayName("재고를 차감할 떄")
    @Nested
    class Deduct {
        @DisplayName("재고가 부족하면 BadRequest 를 반환한다.")
        @Test
        void throwsBadRequest_whenQuantityIsMissing() {
            Product product1 = Product.create(999L, "에어포스1", 10000, 2);

            CoreException exception = assertThrows(CoreException.class, () -> product1.deductQuantity(3));

            assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);

        }

        @DisplayName("요청 수량이 0 이하이면 BadRequest를 반환한다.")
        @ParameterizedTest
        @ValueSource(ints = {0, -1})
        void throwsBadRequest_whenRequestQuantityIsNonPositive(int quantity) {
            Product product1 = Product.create(999L, "에어포스1", 10000, 2);

            CoreException exception = assertThrows(CoreException.class, () -> product1.deductQuantity(quantity));

            assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);

        }

        @DisplayName("재고가 부족하지 않으면 성공한다.")
        @Test
        void deduct_when_sufficient() {
            Product product1 = Product.create(999L, "에어포스1", 10000, 2);

            product1.deductQuantity(1);

            assertThat(product1.getQuantity()).isEqualTo(1);

        }
    }


}
