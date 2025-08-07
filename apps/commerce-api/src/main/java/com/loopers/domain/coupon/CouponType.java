package com.loopers.domain.coupon;


import java.math.BigDecimal;
import java.math.RoundingMode;

public enum CouponType {
    FIXED {
        @Override
        public BigDecimal calculate(BigDecimal totalAmount, BigDecimal value){
            return totalAmount.subtract(value) ;
        }

    },
    PERCENTAGE {
        @Override
        public BigDecimal calculate(BigDecimal totalAmount, BigDecimal value) {
            return totalAmount
                    .multiply(value)
                    .divide(BigDecimal.valueOf(100), 0, RoundingMode.FLOOR);
        }

    };

    public abstract BigDecimal calculate(BigDecimal totalAmount, BigDecimal value);
}
