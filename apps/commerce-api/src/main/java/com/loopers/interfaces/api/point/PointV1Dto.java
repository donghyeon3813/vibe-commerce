package com.loopers.interfaces.api.point;

import com.loopers.application.point.PointChargeInfo;
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

    public record PointChargeResponse(int point) {
        public static PointChargeResponse from(PointChargeInfo info) {
            return new PointChargeResponse(info.getPoint());
        }
    }

    public record PointChargeRequest(int point, String userId) {
        public static PointChargeRequest of(int point, String userId) {
            return new PointChargeRequest(point, userId);
        }
    }
}
