package com.loopers.application.point;

import com.loopers.domain.point.PointModel;


public record PointInfo(int point) {
    public static PointInfo from(PointModel point) {
        return new PointInfo(point.getPoint());
    }
}
