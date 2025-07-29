package com.loopers.infrastructure.like;

import com.loopers.domain.like.Like;
import com.loopers.domain.like.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LikeRepositoryImpl implements LikeRepository {

    private final LikeJpaRepository likeJpaRepository;

    @Override
    public void resister(Like like) {
        likeJpaRepository.save(like);
    }

    @Override
    public Optional<Like> getLike(Long userUid, Long productUid) {

        return likeJpaRepository.findByUserUidAndProductUid(userUid, productUid);
    }

    @Override
    public void deleteLike(Like like) {
        likeJpaRepository.delete(like);
    }
}
