package com.loopers.interfaces.api.point;

import com.loopers.application.point.PointInfo;

public class PointV1Dto {
    public record PointInfoResponse(Integer point) {
        public static PointInfoResponse from(PointInfo info) {
            return new PointInfoResponse(info.point());
        }

    }

    public record PointInfoRequest(String userId) {
        public static PointInfoRequest of(String userId) {
            return new PointInfoRequest(userId);
        }
    }
}
