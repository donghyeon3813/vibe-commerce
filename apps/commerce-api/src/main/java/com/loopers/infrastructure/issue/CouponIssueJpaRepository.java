package com.loopers.infrastructure.issue;

import com.loopers.domain.issue.CouponIssue;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CouponIssueJpaRepository extends JpaRepository<CouponIssue, Long> {
    List<CouponIssue> findByUserUid(Long userUid);

    List<CouponIssue> findByUseFlagAndUserUid(int useFlag,Long userUid);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM CouponIssue c WHERE c.id = :id AND c.useFlag = :useFlag")
    Optional<CouponIssue> findByIdAndUseFlagForUpdate(@Param("id") Long id, @Param("useFlag") int useFlag);
}
