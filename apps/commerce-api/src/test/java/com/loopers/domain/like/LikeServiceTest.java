package com.loopers.domain.like;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LikeServiceTest {

    @Mock
    LikeRepository likeRepository;

    @InjectMocks
    LikeService likeService;

    @DisplayName("like 를 등록할 때")
    @Nested
    class Register {

        @DisplayName("이미 등록되어 있다면 like 저장을 수행하지 않는다.")
        @Test
        void skipLikeSave_whenAlreadyLiked() {
            Long userId = 1L;
            Long productId = 1L;
            Like existingLike = Like.create(userId, productId);
            when(likeRepository.getLike(userId, productId)).thenReturn(Optional.of(existingLike));

            likeService.resist(userId, productId);

            then(likeRepository).should(never()).resister(any());
        }

        @DisplayName("이미 등록 되어 있지 않으면 등록에 성공한다.")
        @Test
        void saveLike_whenNotAlreadyExists() {
            Long userId = 1L;
            Long productId = 1L;
            when(likeRepository.getLike(userId, productId)).thenReturn(Optional.empty());

            likeService.resist(userId, productId);

            then(likeRepository).should().resister(any());
        }
    }

    @DisplayName("like 를 해제할 때")
    @Nested
    class Delete {
        @DisplayName("이미 등록되어 있지 않다면 delete 를 수행하지 않는다.")
        @Test
        void notDelete_whenLikeNotExists() {
            Long userId = 1L;
            Long productId = 1L;
            when(likeRepository.getLike(userId, productId)).thenReturn(Optional.empty());

            likeService.unLike(userId, productId);

            then(likeRepository).should(never()).deleteLike(any());
        }

        @DisplayName("like 가 등록되어 있다면 delete 를 수행한다.")
        @Test
        void delete_whenLikeExists() {
            Long userId = 1L;
            Long productId = 1L;
            when(likeRepository.getLike(userId, productId)).thenReturn(Optional.of(Like.create(userId, productId)));

            likeService.unLike(userId, productId);

            then(likeRepository).should().deleteLike(any());
        }
    }


}
