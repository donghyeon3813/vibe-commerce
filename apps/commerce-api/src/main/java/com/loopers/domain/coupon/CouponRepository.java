package com.loopers.domain.coupon;

import java.util.List;
import java.util.Optional;

public interface CouponRepository {
    List<Coupon> findByIdIn(List<Long> couponIssueIds);

    Optional<Coupon> findById(Long couponUid);
}
