package com.loopers.domain.issue;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CouponIssueService {
    private final CouponIssueRepository couponIssueRepository;

    public List<CouponIssue> getCouponIssueList(Long userUid) {
        return couponIssueRepository.findByUserUidAndUseFlag(0, userUid);
    }
}
