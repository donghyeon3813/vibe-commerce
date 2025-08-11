package com.loopers.domain.point;

import java.util.Optional;

public interface PointRepository {
    Optional<PointModel> findByUserUid(Long userUid);

    PointModel save(PointModel pointModel);

    Optional<PointModel> findByUserUidForUpdate(Long userUid);
}
