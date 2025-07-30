package com.loopers.domain.like;

import java.util.Optional;

public interface LikeRepository {

    void resister(Like like);

    Optional<Like> getLike(Long userUid, Long productUid);

    void deleteLike(Like like);

    int getCountByProductUid(Long productUid);
}
