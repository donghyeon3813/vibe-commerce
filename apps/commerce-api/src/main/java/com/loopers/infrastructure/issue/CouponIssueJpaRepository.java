package com.loopers.infrastructure.issue;

import com.loopers.domain.issue.CouponIssue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CouponIssueJpaRepository extends JpaRepository<CouponIssue, Long> {
    List<CouponIssue> findByUserUid(Long userUid);

    List<CouponIssue> findByUseFlagAndUserUid(int useFlag,Long userUid);
}
