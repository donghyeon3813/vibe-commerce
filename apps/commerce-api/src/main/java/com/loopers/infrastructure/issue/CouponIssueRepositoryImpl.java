package com.loopers.infrastructure.issue;

import com.loopers.domain.issue.CouponIssue;
import com.loopers.domain.issue.CouponIssueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CouponIssueRepositoryImpl implements CouponIssueRepository {

    private final CouponIssueJpaRepository couponIssueJpaRepository;

    @Override
    public List<CouponIssue> findByUserUidAndUseFlag(int useFlag, Long userUid) {
        return couponIssueJpaRepository.findByUseFlagAndUserUid(useFlag, userUid);
    }

    @Override
    public Optional<CouponIssue> findByIdAndUseFlagForUpdate(Long couponId, int useFlag) {
        return couponIssueJpaRepository.findByIdAndUseFlagForUpdate(couponId, useFlag);
    }

    @Override
    public Optional<CouponIssue> findByIdAndUseFlag(Long couponId, int i) {
        return couponIssueJpaRepository.findByIdAndUseFlag(couponId, i);
    }
}
