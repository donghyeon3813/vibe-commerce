package com.loopers.domain.like;

import java.util.List;
import java.util.Optional;

public interface LikeRepository {

    void resister(Like like);

    Optional<Like> getLike(Long userUid, Long productUid);

    void deleteLike(Like like);

    int getCountByProductUid(Long productUid);

    List<Like> findByUserUid(Long userUid);
}
