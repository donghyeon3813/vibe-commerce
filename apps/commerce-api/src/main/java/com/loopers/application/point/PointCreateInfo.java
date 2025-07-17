package com.loopers.application.point;

import lombok.Getter;

@Getter
public class PointCreateInfo {
    private Long userId;
    private int point;

    public PointCreateInfo(Long userId, int point) {
        this.userId = userId;
        this.point = point;
    }

    public static PointCreateInfo of(Long userId, int point) {
        return new PointCreateInfo(userId, point);
    }
}
