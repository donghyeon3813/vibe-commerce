package com.loopers.infrastructure.coupon;

import com.loopers.domain.coupon.Coupon;
import com.loopers.domain.coupon.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CouponRepositoryImpl implements CouponRepository {

    private final CouponJpaRepository couponJPaRepository;

    @Override
    public List<Coupon> findByIdIn(List<Long> couponIssueIds) {
        return couponJPaRepository.findByIdIn(couponIssueIds);
    }
}
