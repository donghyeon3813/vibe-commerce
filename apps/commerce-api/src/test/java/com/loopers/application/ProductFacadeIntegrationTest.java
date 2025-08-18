package com.loopers.application;

import com.loopers.application.product.ProductCommand;
import com.loopers.application.product.ProductFacade;
import com.loopers.application.product.ProductInfo;
import com.loopers.domain.brand.Brand;
import com.loopers.domain.like.Like;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductData;
import com.loopers.domain.productlike.ProductLike;
import com.loopers.infrastructure.brand.BrandJpaRepository;
import com.loopers.infrastructure.like.LikeJpaRepository;
import com.loopers.infrastructure.product.ProductJpaRepository;
import com.loopers.infrastructure.productlike.ProductLikeJpaRepository;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
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
    @Autowired
    private BrandJpaRepository brandJpaRepository;
    @Autowired
    private LikeJpaRepository likeJpaRepository;
    @Autowired
    private ProductLikeJpaRepository productLikeJpaRepository;

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @DisplayName("상품 목록을 조회할 때")
    @Nested
    class InfoList {
        @DisplayName("조건에 맞는 데이터가 없으면 빈 목록을 반환한다.")
        @Test
        void returnsEmpty_whenUserExistsAndChargesSuccessfully() {
            ProductCommand.ListInfoRequest getList = ProductCommand.ListInfoRequest.of(
                    999999L,
                    ProductCommand.ListInfoRequest.Sort.LIKE_DESC,
                    0,
                    20
            );

            ProductInfo.ProductListInfo listInfo = productFacade.getProductList(getList);

            assertThat(listInfo).isNotNull();
            assertThat(listInfo.products().size()).isEqualTo(0);


        }
        @DisplayName("상품이 있으면 조건에 맞는 데이터를 반환한다.")
        @Test
        void returnsProductListInfo_whenUserExistsAndChargesSuccessfully() {
            Brand brand = Brand.create("무신사");
            Brand savedBrand = brandJpaRepository.save(brand);
            Product product1 = productJpaRepository.save(Product.create(savedBrand.getId(), "상의", 1000, 5));  // name: 상의
            Product product2 = productJpaRepository.save(Product.create(savedBrand.getId(), "하의", 1300, 5));  // name: 하의
            Product product3 = productJpaRepository.save(Product.create(savedBrand.getId(), "신발", 1200, 5));   // name: 신발

            likeJpaRepository.save(new Like(9999L, product2.getId()));
            likeJpaRepository.save(new Like(9998L, product2.getId()));
            likeJpaRepository.save(new Like(9997L, product3.getId()));
            ProductLike save = productLikeJpaRepository.save(ProductLike.of(product1.getId(), 0L, savedBrand.getId()));
            productLikeJpaRepository.save(ProductLike.of(product2.getId(), 2L,savedBrand.getId()));
            productLikeJpaRepository.save(ProductLike.of(product3.getId(), 1L,savedBrand.getId()));

            ProductCommand.ListInfoRequest getList = ProductCommand.ListInfoRequest.of(
                    savedBrand.getId(),
                    ProductCommand.ListInfoRequest.Sort.LIKE_DESC,
                    0,
                    20
            );
            ProductInfo.ProductListInfo listInfo = productFacade.getProductList(getList);

            assertThat(listInfo).isNotNull();
            assertThat(listInfo.products().size()).isEqualTo(3);

        }
        @DisplayName("정렬 조건에 따라 상품 리스트가 정확히 정렬되어 반환된다")
        @Test
        void returnsSortedProductList_whenSortConditionIsGiven() {

            Brand brand = Brand.create("무신사");
            Brand savedBrand = brandJpaRepository.save(brand);

            Product product1 = productJpaRepository.save(Product.create(savedBrand.getId(), "상의", 1000, 5));  // name: 상의
            Product product2 = productJpaRepository.save(Product.create(savedBrand.getId(), "하의", 1300, 5));  // name: 하의
            Product product3 = productJpaRepository.save(Product.create(savedBrand.getId(), "신발", 1200, 5));   // name: 신발

            likeJpaRepository.save(new Like(9999L, product2.getId()));
            likeJpaRepository.save(new Like(9998L, product2.getId()));
            likeJpaRepository.save(new Like(9997L, product3.getId()));
            productLikeJpaRepository.save(ProductLike.of(product1.getId(), 0, savedBrand.getId()));
            productLikeJpaRepository.save(ProductLike.of(product2.getId(), 2, savedBrand.getId()));
            productLikeJpaRepository.save(ProductLike.of(product3.getId(), 1, savedBrand.getId()));
            ProductCommand.ListInfoRequest latest = ProductCommand.ListInfoRequest.of(
                    savedBrand.getId(), ProductCommand.ListInfoRequest.Sort.LATEST, 0, 10);
            ProductInfo.ProductListInfo latestSortedList = productFacade.getProductList(latest);

            ProductCommand.ListInfoRequest priceDec = ProductCommand.ListInfoRequest.of(
                    savedBrand.getId(), ProductCommand.ListInfoRequest.Sort.PRICE_ASC, 0, 10);
            ProductInfo.ProductListInfo amountSortedList = productFacade.getProductList(priceDec);

            ProductCommand.ListInfoRequest likeDec = ProductCommand.ListInfoRequest.of(
                    savedBrand.getId(), ProductCommand.ListInfoRequest.Sort.LIKE_DESC, 0, 10);
            ProductInfo.ProductListInfo likeCountSortedList = productFacade.getProductList(likeDec);

            assertThat(latestSortedList.products())
                    .extracting(ProductData::productName)
                    .containsExactly("신발", "하의", "상의"); // like: 2, 1, 0

            assertThat(amountSortedList.products())
                    .extracting(ProductData::productName)
                    .containsExactly("상의", "신발", "하의"); // like 0, 2, 1

            assertThat(likeCountSortedList.products())
                    .extracting(ProductData::productName)
                    .containsExactly("하의", "신발", "상의"); // like 1, 2, 0
        }

        @DisplayName("상품 요청 페이지에 따라 정확한 갯수가 반환된다")
        @Test
        void returnsSortedProductList_whenPage() {

            Brand brand = Brand.create("무신사");
            Brand savedBrand = brandJpaRepository.save(brand);
            //page1
            Product product1 = productJpaRepository.save(Product.create(savedBrand.getId(), "상의", 1000, 5));  // name: 상의
            Product product2 = productJpaRepository.save(Product.create(savedBrand.getId(), "하의", 1300, 5));  // name: 하의
            Product product3 = productJpaRepository.save(Product.create(savedBrand.getId(), "신발", 1200, 5));   // name: 신발
            Product product4 = productJpaRepository.save(Product.create(savedBrand.getId(), "신발", 1400, 5));   // name: 신발
            Product product5 = productJpaRepository.save(Product.create(savedBrand.getId(), "신발", 1500, 5));   // name: 신발
            Product product6 = productJpaRepository.save(Product.create(savedBrand.getId(), "신발", 1600, 5));   // name: 신발
            Product product7 = productJpaRepository.save(Product.create(savedBrand.getId(), "신발", 1700, 5));   // name: 신발
            likeJpaRepository.save(new Like(9999L, product2.getId()));
            likeJpaRepository.save(new Like(9998L, product2.getId()));
            likeJpaRepository.save(new Like(9997L, product3.getId()));
            likeJpaRepository.save(new Like(9997L, product4.getId()));
            likeJpaRepository.save(new Like(9997L, product5.getId()));
            likeJpaRepository.save(new Like(9997L, product6.getId()));
            likeJpaRepository.save(new Like(9997L, product7.getId()));
            productLikeJpaRepository.save(ProductLike.of(product1.getId(), 0, savedBrand.getId()));
            productLikeJpaRepository.save(ProductLike.of(product2.getId(), 2, savedBrand.getId()));
            productLikeJpaRepository.save(ProductLike.of(product3.getId(), 1, savedBrand.getId()));
            productLikeJpaRepository.save(ProductLike.of(product4.getId(), 1, savedBrand.getId()));
            productLikeJpaRepository.save(ProductLike.of(product5.getId(), 1, savedBrand.getId()));
            productLikeJpaRepository.save(ProductLike.of(product6.getId(), 1, savedBrand.getId()));
            productLikeJpaRepository.save(ProductLike.of(product7.getId(), 1, savedBrand.getId()));


            ProductCommand.ListInfoRequest request = ProductCommand.ListInfoRequest.of(
                    savedBrand.getId(), ProductCommand.ListInfoRequest.Sort.LATEST, 0, 5);
            ProductInfo.ProductListInfo pageZeroList = productFacade.getProductList(request);

            ProductCommand.ListInfoRequest request2 = ProductCommand.ListInfoRequest.of(
                    savedBrand.getId(), ProductCommand.ListInfoRequest.Sort.LATEST, 1, 5);
            ProductInfo.ProductListInfo pageOneList = productFacade.getProductList(request2);

            assertThat(pageZeroList.products().size()).isEqualTo(5);
            assertThat(pageOneList.products().size()).isEqualTo(2);
        }
    }

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
