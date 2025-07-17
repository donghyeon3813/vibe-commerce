package com.loopers.application.point;

import com.loopers.domain.point.PointModel;
import lombok.Getter;

@Getter
public class PointChargeInfo {
    private int point;

    public PointChargeInfo(int point) {
        this.point = point;
    }

    public static PointChargeInfo from(PointModel point) {
        return new PointChargeInfo(point.getPoint());
    }
}
