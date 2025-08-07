package com.loopers.interfaces.api.point;

import com.loopers.application.point.PointChargeInfo;
import com.loopers.application.point.PointInfo;

import java.math.BigDecimal;

public class PointV1Dto {
    public record PointInfoResponse(java.math.BigDecimal point) {
        public static PointInfoResponse from(PointInfo info) {
            return new PointInfoResponse(info.point());
        }

    }

    public record PointInfoRequest(String userId) {
        public static PointInfoRequest of(String userId) {
            return new PointInfoRequest(userId);
        }
    }

    public record PointChargeResponse(BigDecimal point) {
        public static PointChargeResponse from(PointChargeInfo info) {
            return new PointChargeResponse(info.getPoint());
        }
    }

    public record PointChargeRequest(BigDecimal point, String userId) {
        public static PointChargeRequest of(int point, String userId) {
            return new PointChargeRequest(BigDecimal.valueOf(point), userId);
        }
    }
}
