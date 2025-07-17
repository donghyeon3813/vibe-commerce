package com.loopers.domain.point;

public interface PointRepository {
    PointModel findByUserUid(Long userUid);

    PointModel save(PointModel pointModel);
}
