package com.loopers.domain.coupon;

import com.loopers.domain.BaseEntity;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "coupon")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Coupon extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private CouponType couponType;
    private BigDecimal value;

    private Coupon(CouponType couponType, BigDecimal value) {
        this.couponType = couponType;
        this.value = value;
    }

    public static Coupon create(CouponType couponType, BigDecimal value) {
        if (couponType == null) {
            throw new CoreException(ErrorType.BAD_REQUEST, "CouponType 이 Null 이 될 수 없습니다.");
        }
        if (value == null || value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new CoreException(ErrorType.BAD_REQUEST, "value 는 0보다 커야 합니다.");
        }
        return new Coupon(couponType, value);
    }
}
