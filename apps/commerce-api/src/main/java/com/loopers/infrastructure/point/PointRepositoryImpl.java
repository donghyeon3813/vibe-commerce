package com.loopers.infrastructure.point;

import com.loopers.domain.point.PointModel;
import com.loopers.domain.point.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PointRepositoryImpl implements PointRepository {
    private final PointJpaRepostiroy pointJpaRepostiroy;

    @Override
    public PointModel findByUserUid(Long userUid) {
        return pointJpaRepostiroy.findByUserUid(userUid);
    }

    @Override
    public PointModel save(PointModel pointModel) {
        return pointJpaRepostiroy.save(pointModel);
    }
}
