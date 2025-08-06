package com.loopers.infrastructure.point;

import com.loopers.domain.point.PointModel;
import com.loopers.domain.point.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PointRepositoryImpl implements PointRepository {
    private final PointJpaRepository pointJpaRepository;

    @Override
    public Optional<PointModel> findByUserUid(Long userUid) {
        return pointJpaRepository.findByUserUid(userUid);
    }

    @Override
    public PointModel save(PointModel pointModel) {
        return pointJpaRepository.save(pointModel);
    }

    @Override
    public Optional<PointModel> findByUserUidForUpdate(Long userUid) {
        return pointJpaRepository.findByUserUidForUpdate(userUid);
    }
}
