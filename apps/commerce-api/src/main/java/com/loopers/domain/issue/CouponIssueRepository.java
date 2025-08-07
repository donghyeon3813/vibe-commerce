package com.loopers.domain.issue;

import java.util.List;
import java.util.Optional;

public interface CouponIssueRepository {

    List<CouponIssue> findByUserUidAndUseFlag(int UserFlag, Long userUid);

    Optional<CouponIssue> findByIdAndUseFlagForUpdate(Long couponId, int useFlag);
}
