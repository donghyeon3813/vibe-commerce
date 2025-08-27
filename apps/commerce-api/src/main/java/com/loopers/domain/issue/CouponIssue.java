package com.loopers.domain.issue;

import com.loopers.domain.BaseEntity;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "coupon_issue")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CouponIssue extends BaseEntity {

    private Long couponUid;

    private Long userUid;

    private int useFlag;

    private CouponIssue(Long couponUid, Long userUid, int useFlag) {
        this.couponUid = couponUid;
        this.userUid = userUid;
        this.useFlag = useFlag;
    }

    public static CouponIssue of(Long couponUid, Long userUid) {
        if (userUid == null) {
            throw new CoreException(ErrorType.BAD_REQUEST, "userUid 는 null 일 수 없습니다.");
        }
        if (couponUid == null) {
            throw new CoreException(ErrorType.BAD_REQUEST, "couponUid 는 null 일 수 없습니다.");
        }
        return new CouponIssue(couponUid, userUid, 0);
    }
    public void use(){
        if(useFlag == 1){
            throw new CoreException(ErrorType.BAD_REQUEST, "이미 사용된 쿠폰은 사용할 수 없습니다.");
        }
        useFlag = 1;
    }
    public void revoke(){
        useFlag = 0;
    }
}
