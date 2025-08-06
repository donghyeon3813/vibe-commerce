package com.loopers.domain.coupon;

import java.util.List;

public interface CouponRepository {
    List<Coupon> findByIdIn(List<Long> couponIssueIds);
}
