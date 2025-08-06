package com.loopers.infrastructure.point;

import com.loopers.domain.point.PointModel;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PointJpaRepository extends JpaRepository<PointModel, Long> {
    Optional<PointModel> findByUserUid(Long userUid);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from PointModel p where p.userUid = :userUid")
    Optional<PointModel> findByUserUidForUpdate(@Param("userUid") Long userUid);
}
