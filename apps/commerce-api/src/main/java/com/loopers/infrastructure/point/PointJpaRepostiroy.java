package com.loopers.infrastructure.point;

import com.loopers.domain.point.PointModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointJpaRepostiroy extends JpaRepository<PointModel, Long> {
    PointModel findByUserUid(Long userUid);
}
