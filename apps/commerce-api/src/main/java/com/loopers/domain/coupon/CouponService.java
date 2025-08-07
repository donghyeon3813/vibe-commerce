package com.loopers.domain.coupon;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
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

    public Coupon getCoupon(Long couponUid) {
        return couponRepository.findById(couponUid).orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "쿠폰을 찾을 수 없습니다."));
    }
}
