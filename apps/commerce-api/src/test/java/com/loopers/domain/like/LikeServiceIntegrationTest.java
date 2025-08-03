package com.loopers.domain.like;

import com.loopers.infrastructure.like.LikeJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class LikeServiceIntegrationTest {

    @Autowired
    LikeService likeService;

    @Autowired
    LikeJpaRepository likeJpaRepository;

    @DisplayName("like count를 조회할 떄")
    @Nested
    class Count {
        @DisplayName("일치 하는 값이 없다면 0을 반환한다.")
        @Test
        void returnsZero_whenLikeNotExists() {
            Long productId = 1L;
            int countByProductUid = likeService.getCountByProductUid(productId);

            assertThat(countByProductUid).isEqualTo(0);

        }

        @DisplayName("일치 하는 값이 있다면 해당하는 개수만큼 반환한다.")
        @Test
        void returnsLikeCount_whenLikeExists() {
            Long productId = 1L;
            likeJpaRepository.save(Like.create(13L, productId));
            likeJpaRepository.save(Like.create(14L, productId));

            int countByProductUid = likeService.getCountByProductUid(productId);

            assertThat(countByProductUid).isEqualTo(2);

        }

    }
    @DisplayName("like한 상품을 조회할 때")
    @Nested
    class Info {
        @DisplayName("일치하는 UserUid가 없단면 빈항목을 반환한다.")
        @Test
        void returnsEmpty_whenUseridNotExists() {

            Long userId = 9999L;

            List<Like> likes = likeService.findByUserUid(userId);

            assertThat(likes).isEmpty();
        }


    }
}
