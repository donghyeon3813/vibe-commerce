package com.loopers.domain.like;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;

    public void resist(Long userUid, Long productUid) {
        Optional<Like> findLike = likeRepository.getLike(userUid, productUid);
        if (findLike.isEmpty()) {
            Like like = Like.create(userUid, productUid);
            likeRepository.resister(like);
        }
    }

    public void unLike(Long userId, Long productId) {
        Optional<Like> findLike = likeRepository.getLike(userId, productId);
        findLike.ifPresent(likeRepository::deleteLike);
    }

    public int getCountByProductUid(Long productId) {
        return likeRepository.getCountByProductUid(productId);
    }
}
