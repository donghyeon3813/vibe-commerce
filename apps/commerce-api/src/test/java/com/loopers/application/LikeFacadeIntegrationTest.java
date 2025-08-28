package com.loopers.application;

import com.loopers.application.like.LikeCommand;
import com.loopers.application.like.LikeFacade;
import com.loopers.application.product.ProductInfo;
import com.loopers.domain.brand.Brand;
import com.loopers.domain.like.Like;
import com.loopers.domain.product.Product;
import com.loopers.domain.productlike.ProductLike;
import com.loopers.domain.user.Gender;
import com.loopers.domain.user.UserModel;
import com.loopers.infrastructure.brand.BrandJpaRepository;
import com.loopers.infrastructure.like.LikeJpaRepository;
import com.loopers.infrastructure.product.ProductJpaRepository;
import com.loopers.infrastructure.productlike.ProductLikeJpaRepository;
import com.loopers.infrastructure.user.UserJpaRepository;
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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class LikeFacadeIntegrationTest {

    @Autowired
    private LikeFacade likeFacade;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private LikeJpaRepository likeJpaRepository;

    @Autowired
    private ProductJpaRepository productJpaRepository;

    @Autowired
    private BrandJpaRepository brandJpaRepository;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;
    @Autowired
    private ProductLikeJpaRepository productLikeJpaRepository;

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @DisplayName("like 를 등록 했을 때")
    @Nested
    class Register {

        @DisplayName("user 가 없으면 등록에 NOT_FOUND 를 반환한다.")
        @Test
        void throwsException_whenUserIdInvalid() {

            Product product = productJpaRepository.save(Product.create(1L, "name", 1000, 5));

            String userId = "test9998";
            Long productUid = product.getId();
            LikeCommand.RegisterDto registerDto = LikeCommand.RegisterDto.of(userId, productUid);

            CoreException exception = assertThrows(CoreException.class, () -> {
                likeFacade.register(registerDto);
            });

            assertThat(exception.getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
        }

        @DisplayName("product 가 없으면 등록에 NOT_FOUND 를 반환한다.")
        @Test
        void throwsException_whenProductIdInvalid() {
            UserModel userModel = UserModel.CreateUser("test9998", "test@test.com", Gender.MALE.name(), "2025-07-13");
            UserModel saveUser = userJpaRepository.save(userModel);

            String userId = "test9998";
            Long productUid = 100L;
            LikeCommand.RegisterDto registerDto = LikeCommand.RegisterDto.of(userId, productUid);

            CoreException exception = assertThrows(CoreException.class, () -> {
                likeFacade.register(registerDto);
            });
            assertThat(saveUser.getUserId()).isEqualTo(userId);
            assertThat(exception.getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
        }

        @DisplayName("like 가 이미 등록되어 있으면 예외가 발생하지 않는다.")
        @Test
        void notThrowException_whenAlreadyExists() {
            UserModel userModel = UserModel.CreateUser("test9998", "test@test.com", Gender.MALE.name(), "2025-07-13");
            UserModel saveUser = userJpaRepository.save(userModel);
            Product product = productJpaRepository.save(Product.create(1L, "name", 1000, 5));
            Long userUid = saveUser.getId();
            String userId = "test9998";
            Long productUid = product.getId();
            likeJpaRepository.save(Like.create(userUid, productUid));
            Optional<Like> like = likeJpaRepository.findByUserUidAndProductUid(userUid, productUid);
            LikeCommand.RegisterDto registerDto = LikeCommand.RegisterDto.of(userId, productUid);

            assertThat(like).isPresent();

            assertThatCode(() -> likeFacade.register(registerDto))
                    .doesNotThrowAnyException();
        }

        @DisplayName("like 가 등록되어 있지 않으면 등록에 성공한다.")
        @Test
        void notThrowException_whenNotExists() {
            UserModel userModel = UserModel.CreateUser("test9998", "test@test.com", Gender.MALE.name(), "2025-07-13");
            UserModel saveUser = userJpaRepository.save(userModel);
            Product product = productJpaRepository.save(Product.create(1L, "name", 1000, 5));
            Long userUid = saveUser.getId();
            String userId = "test9998";
            Long productUid = product.getId();

            Optional<Like> like = likeJpaRepository.findByUserUidAndProductUid(userUid, productUid);
            assertThat(like).isEmpty();

            LikeCommand.RegisterDto registerDto = LikeCommand.RegisterDto.of(userId, productUid);
            likeFacade.register(registerDto);

            Optional<Like> savedLike = likeJpaRepository.findByUserUidAndProductUid(userUid, productUid);
            assertThat(savedLike).isPresent();
            assertThat(savedLike.get().getUserUid()).isEqualTo(userUid);
            assertThat(savedLike.get().getProductUid()).isEqualTo(productUid);
        }
    }

    @DisplayName("like 를 해제할 때")
    @Nested
    class Unregister {
        @DisplayName("user 가 없으면 notFound 가 발생한다.")
        @Test
        void throwsException_whenUserNotFound() {
            Product product = productJpaRepository.save(Product.create(1L, "name", 1000, 5));

            String userId = "test9997";
            Long productUid = product.getId();
            LikeCommand.DeleteDto deleteDto = LikeCommand.DeleteDto.of(userId, productUid);

            CoreException exception = assertThrows(CoreException.class, () -> {
                likeFacade.delete(deleteDto);
            });

            assertThat(exception.getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
        }

        @DisplayName("이미 like 가 존재하지 않으면 Exception 이 발생하지 않는다.")
        @Test
        void notThrowException_whenNotExists() {
            UserModel userModel = UserModel.CreateUser("test9998", "test@test.com", Gender.MALE.name(), "2025-07-13");
            UserModel saveUser = userJpaRepository.save(userModel);
            Product product = productJpaRepository.save(Product.create(1L, "name", 1000, 5));
            Long userUid = saveUser.getId();
            String userId = "test9998";
            Long productUid = product.getId();

            Optional<Like> like = likeJpaRepository.findByUserUidAndProductUid(userUid, productUid);
            assertThat(like).isEmpty();

            LikeCommand.DeleteDto registerDto = LikeCommand.DeleteDto.of(userId, productUid);

            assertThatCode(() -> likeFacade.delete(registerDto))
                    .doesNotThrowAnyException();
        }

        @DisplayName("like 가 존재하면 삭제에 성공한다.")
        @Test
        void notThrowException_whenExists() {
            UserModel userModel = UserModel.CreateUser("test9998", "test@test.com", Gender.MALE.name(), "2025-07-13");
            UserModel saveUser = userJpaRepository.save(userModel);
            Product product = productJpaRepository.save(Product.create(1L, "name", 1000, 5));
            Long userUid = saveUser.getId();
            String userId = "test9998";
            Long productUid = product.getId();
            likeJpaRepository.save(Like.create(userUid, productUid));
            Optional<Like> like = likeJpaRepository.findByUserUidAndProductUid(userUid, productUid);
            assertThat(like).isPresent();

            LikeCommand.DeleteDto deleteDto = LikeCommand.DeleteDto.of(userId, productUid);
            likeFacade.delete(deleteDto);

            Optional<Like> findLike = likeJpaRepository.findByUserUidAndProductUid(userUid, productUid);
            assertThat(findLike).isEmpty();

        }

    }
    @DisplayName("like 된 상품 정보를 조회할 때")
    @Nested
    class Products{
        @Test
        @DisplayName("등록되지 않은 userId로 요청시 NotFound 를 반환한다.")
        void notThrowException_whenNotExists() {

            String userId = "like0001";
            LikeCommand.GetProduct request = LikeCommand.GetProduct.of(userId);

            CoreException exception = assertThrows(CoreException.class,() -> likeFacade.getProducts(request));

            assertThat(exception.getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
        }
        @DisplayName("like 가 없으면 빈 목록을 반환한다.")
        @Test
        void returnsEmptyList_whenLikeNotExists() {
            UserModel userModel = UserModel.CreateUser("like0001", "test@test.com", "MALE", "2025-07-13");
            userJpaRepository.save(userModel);
            String userId = "like0001";
            LikeCommand.GetProduct request = LikeCommand.GetProduct.of(userId);

            ProductInfo.ProductListInfo products = likeFacade.getProducts(request);

            assertThat(products).isNotNull();
            assertThat(products.products()).isEmpty();

        }
        @DisplayName("좋아요한 목록중 상품이 존재하지 않으면 해당 상품은 제외한다.")
        @Test
        void returnsOnlyExistingProductsFromLikes() {
            UserModel userModel = UserModel.CreateUser("test9998", "test@test.com", Gender.MALE.name(), "2025-07-13");
            UserModel saveUser = userJpaRepository.save(userModel);
            Brand brand = Brand.create("무신사");
            Brand savedBrand = brandJpaRepository.save(brand);
            Product product = productJpaRepository.save(Product.create(savedBrand.getId(), "신발", 1000, 5));
            Long userUid = saveUser.getId();
            String userId = "test9998";
            Long productUid = product.getId();
            likeJpaRepository.save(Like.create(userUid, productUid));
            likeJpaRepository.save(Like.create(userUid, 9999L));
            LikeCommand.GetProduct request = LikeCommand.GetProduct.of(userId);

            ProductInfo.ProductListInfo products = likeFacade.getProducts(request);

            assertThat(products).isNotNull();
            assertThat(products.products()).hasSize(1);
            assertThat(products.products().get(0).productName()).isEqualTo("신발");

        }
        @DisplayName("좋아요한 목록중 브랜드가 존재하지 않으면 해당 상품은 제외한다.")
        @Test
        void returnsOnlyExistingBrandFromLikes() {
            UserModel userModel = UserModel.CreateUser("test9998", "test@test.com", Gender.MALE.name(), "2025-07-13");
            UserModel saveUser = userJpaRepository.save(userModel);
            Product product = productJpaRepository.save(Product.create(9999L, "신발", 1000, 5));
            Long userUid = saveUser.getId();
            String userId = "test9998";
            Long productUid = product.getId();
            likeJpaRepository.save(Like.create(userUid, productUid));
            likeJpaRepository.save(Like.create(userUid, 9999L));
            LikeCommand.GetProduct request = LikeCommand.GetProduct.of(userId);

            ProductInfo.ProductListInfo products = likeFacade.getProducts(request);

            assertThat(products).isNotNull();
            assertThat(products.products()).hasSize(0);

        }
    }
    @DisplayName("like를 동시에 요청할때")
    @Nested
    class Concurrency {
        @DisplayName("좋아요 요청이 실제 등록된 횟수만큼 좋아요 수가 정확히 증가하여 조회된다.")
        @Test
        void itIncrementsLikeCountAccordingToRequestCount() throws InterruptedException {
            // given

            UserModel saveUser1 = userJpaRepository.save(UserModel.CreateUser("test1000", "test@test.com", Gender.MALE.name(), "2025-07-13"));
            UserModel saveUser2 = userJpaRepository.save(UserModel.CreateUser("test1001", "test@test.com", Gender.MALE.name(), "2025-07-13"));
            UserModel saveUser3 = userJpaRepository.save(UserModel.CreateUser("test1002", "test@test.com", Gender.MALE.name(), "2025-07-13"));
            UserModel saveUser4 = userJpaRepository.save(UserModel.CreateUser("test1003", "test@test.com", Gender.MALE.name(), "2025-07-13"));
            Product product = productJpaRepository.save(Product.create(1L, "name", 1000, 5));
            productLikeJpaRepository.save(ProductLike.of(product.getId(), 0L, 1L));
            Long productUid = product.getId();
            LikeCommand.RegisterDto[] registerDtos = new LikeCommand.RegisterDto[]{
                    LikeCommand.RegisterDto.of(saveUser1.getUserId(), productUid),
                    LikeCommand.RegisterDto.of(saveUser2.getUserId(), productUid),
                    LikeCommand.RegisterDto.of(saveUser3.getUserId(), productUid),
                    LikeCommand.RegisterDto.of(saveUser4.getUserId(), productUid),
                    LikeCommand.RegisterDto.of(saveUser4.getUserId(), productUid)
            };


            int threadCount = 5;
            ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
            CountDownLatch latch = new CountDownLatch(threadCount);

            for (int i = 0; i < threadCount; i++) {
                int finalI = i;
                executorService.submit(() -> {
                    try {
                        likeFacade.register(registerDtos[finalI]);
                    }finally {
                        latch.countDown();
                    }

                });
            }

            latch.await();
            executorService.shutdown();
            executorService.awaitTermination(5, SECONDS);

            await().atMost(5, SECONDS).untilAsserted(() -> {
                Optional<ProductLike> updatedProductLike = productLikeJpaRepository.findById(productUid);
                assertThat(updatedProductLike).isPresent();
                assertThat(updatedProductLike.get().getLikeCount()).isEqualTo(4);
            });
        }

        @DisplayName("좋아요 해제 요청이 발생한 횟수만큼 좋아요 수가 정확히 차감되어 조회된다.")
        @Test
        void itDecrementsLikeCountAccordingToUnlikeRequestCount() throws InterruptedException {
            // given

            UserModel saveUser1 = userJpaRepository.save(UserModel.CreateUser("test1000", "test@test.com", Gender.MALE.name(), "2025-07-13"));
            UserModel saveUser2 = userJpaRepository.save(UserModel.CreateUser("test1001", "test@test.com", Gender.MALE.name(), "2025-07-13"));
            UserModel saveUser3 = userJpaRepository.save(UserModel.CreateUser("test1002", "test@test.com", Gender.MALE.name(), "2025-07-13"));
            UserModel saveUser4 = userJpaRepository.save(UserModel.CreateUser("test1003", "test@test.com", Gender.MALE.name(), "2025-07-13"));
            UserModel saveUser5 = userJpaRepository.save(UserModel.CreateUser("test1004", "test@test.com", Gender.MALE.name(), "2025-07-13"));
            Product product = productJpaRepository.save(Product.create(1L, "name", 1000, 5));
            likeJpaRepository.save(Like.create(saveUser1.getId(), product.getId()));
            likeJpaRepository.save(Like.create(saveUser2.getId(), product.getId()));
            likeJpaRepository.save(Like.create(saveUser3.getId(), product.getId()));
            likeJpaRepository.save(Like.create(saveUser4.getId(), product.getId()));
            likeJpaRepository.save(Like.create(saveUser5.getId(), product.getId()));
            Long productUid = product.getId();

            LikeCommand.DeleteDto[] delteDtos = new LikeCommand.DeleteDto[]{
                    LikeCommand.DeleteDto.of(saveUser1.getUserId(), productUid),
                    LikeCommand.DeleteDto.of(saveUser2.getUserId(), productUid),
                    LikeCommand.DeleteDto.of(saveUser3.getUserId(), productUid),
                    LikeCommand.DeleteDto.of(saveUser4.getUserId(), productUid),
                    LikeCommand.DeleteDto.of(saveUser5.getUserId(), productUid),
            };


            int threadCount = 5;
            ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
            CountDownLatch latch = new CountDownLatch(threadCount);

            for (int i = 0; i < threadCount; i++) {
                int finalI = i;
                executorService.submit(() -> {
                    try {
                        likeFacade.delete(delteDtos[finalI]);
                    }finally {
                        latch.countDown();
                    }

                });
            }

            latch.await();
            executorService.shutdown();
            executorService.awaitTermination(10, SECONDS);

            int resultCount = likeJpaRepository.countByProductUid(productUid);
            assertThat(resultCount).isEqualTo(0);
        }
    }



}
