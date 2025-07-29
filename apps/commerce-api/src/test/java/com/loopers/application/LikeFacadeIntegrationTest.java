package com.loopers.application;

import com.loopers.application.like.LikeCommand;
import com.loopers.application.like.LikeFacade;
import com.loopers.domain.like.Like;
import com.loopers.domain.product.Product;
import com.loopers.domain.user.Gender;
import com.loopers.domain.user.UserModel;
import com.loopers.infrastructure.like.LikeJpaRepository;
import com.loopers.infrastructure.product.ProductJpaRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
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
    private DatabaseCleanUp databaseCleanUp;

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

            String userId = "test9998";
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


}
