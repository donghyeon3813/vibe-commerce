package com.loopers.application.point;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class PointCreateInfo {
    private Long userId;
    private BigDecimal point;

    public PointCreateInfo(Long userId, BigDecimal point) {
        this.userId = userId;
        this.point = point;
    }

    public static PointCreateInfo of(Long userId, BigDecimal point) {
        return new PointCreateInfo(userId, point);
    }
}
