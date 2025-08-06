package com.loopers.domain.coupon;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    public List<Coupon> getCoupons(List<Long> couponIssueIds) {
        return couponRepository.findByIdIn(couponIssueIds);
    }
}
