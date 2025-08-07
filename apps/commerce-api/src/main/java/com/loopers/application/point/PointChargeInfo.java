package com.loopers.application.point;

import com.loopers.domain.point.PointModel;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class PointChargeInfo {
    private BigDecimal point;

    public PointChargeInfo(BigDecimal point) {
        this.point = point;
    }

    public static PointChargeInfo from(PointModel point) {
        return new PointChargeInfo(point.getPoint());
    }
}
