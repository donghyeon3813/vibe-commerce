package com.loopers.domain.productlike;

import com.loopers.domain.like.Like;
import com.loopers.infrastructure.like.LikeJpaRepository;
import com.loopers.infrastructure.productlike.ProductLikeJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
public class ProductLikeServiceIntegrationTest {

    @Autowired
    private ProductLikeService productLikeService;
    @Autowired
    private LikeJpaRepository likeJpaRepository;
    @Autowired
    private ProductLikeJpaRepository productLikeJpaRepository;

    @DisplayName("like count를 조회할 떄")
    @Nested
    class Count {
        @DisplayName("일치 하는 값이 없다면 빈 객체를 반환한다.")
        @Test
        void returnsZero_whenLikeNotExists() {
            Long productId = 1L;
            Optional<ProductLike> productLike = productLikeService.getProductLike(productId);

            assertThat(productLike).isEmpty();

        }

        @DisplayName("일치 하는 상품이 있다면 해당하는 값을 반환한다.")
        @Test
        void returnsLikeCount_whenLikeExists() {
            Long productId = 1L;
            likeJpaRepository.save(Like.create(13L, productId));
            likeJpaRepository.save(Like.create(14L, productId));
            productLikeJpaRepository.save(ProductLike.of(1L, 2, 1L));

            Optional<ProductLike> productLike = productLikeService.getProductLike(productId);

            assertThat(productLike.get().getLikeCount()).isEqualTo(2);

        }

    }
}
