package com.loopers.application.issue.listener;

import lombok.Getter;
import lombok.ToString;

public class CouponIssueEvent {
    @Getter
    @ToString
    public static class UseCouponIssueEvent {
        Long couponIssueId;

        public UseCouponIssueEvent(Long couponIssueId) {
            this.couponIssueId = couponIssueId;
        }
        public static UseCouponIssueEvent of(Long couponIssueId) {
            return new UseCouponIssueEvent(couponIssueId);
        }
    }
}
