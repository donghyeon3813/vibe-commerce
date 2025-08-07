package com.loopers.domain.issue;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CouponIssueService {
    private final CouponIssueRepository couponIssueRepository;

    public List<CouponIssue> getCouponIssueList(Long userUid) {
        return couponIssueRepository.findByUserUidAndUseFlag(0, userUid);
    }

    @Transactional
    public Optional<CouponIssue> findByIdAndUseFlagForUpdate(Long couponId) {
        return couponIssueRepository.findByIdAndUseFlagForUpdate(couponId, 0);
    }
}
