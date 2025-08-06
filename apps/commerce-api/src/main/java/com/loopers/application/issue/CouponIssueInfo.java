package com.loopers.application.issue;

import com.loopers.domain.coupon.Coupon;
import com.loopers.domain.issue.CouponData;

import java.util.List;

public class CouponIssueInfo {
    public record ListInfo(List<Coupon> list) {
        public static ListInfo from(List<Coupon> list) {
            return new ListInfo(list);
        }
    }
}
