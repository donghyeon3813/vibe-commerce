package com.loopers.application.issue;

import com.loopers.domain.coupon.Coupon;
import com.loopers.domain.coupon.CouponService;
import com.loopers.domain.issue.CouponIssue;
import com.loopers.domain.issue.CouponIssueService;
import com.loopers.domain.user.UserModel;
import com.loopers.domain.user.UserService;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CouponIssueFacade {
    private final CouponIssueService couponIssueService;
    private final UserService userService;
    private final CouponService couponService;

    public CouponIssueInfo.ListInfo getList(CouponIssueCommand.GetList request){

        UserModel user = userService.getUser(request.userId());
        if(user == null){
            throw new CoreException(ErrorType.NOT_FOUND, "유저를 찾을 수 없습니다.");
        }

        List<CouponIssue> couponIssues = couponIssueService.getCouponIssueList(user.getId());
        if(couponIssues.isEmpty()){
            return CouponIssueInfo.ListInfo.from(Collections.emptyList());
        }

        List<Long> couponUids = couponIssues.stream().map(CouponIssue::getCouponUid).toList();
        List<Coupon> coupons = couponService.getCoupons(couponUids);

        return CouponIssueInfo.ListInfo.from(coupons);

    }
}
