package com.loopers.application.issue;

public class CouponIssueCommand {
    public record GetList(String userId) {
        public static GetList of(String userId) {
            return new GetList(userId);
        }
    }


}
