package com.loopers.domain.point;

import com.loopers.domain.BaseEntity;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "point")
@DynamicUpdate
public class PointModel extends BaseEntity {
    private Long userUid;
    private BigDecimal point;

    public PointModel(Long userUid, BigDecimal point) {
        this.userUid = userUid;
        this.point = point;
    }

    public static PointModel create(long userUid, BigDecimal point) {
        if (point.compareTo(BigDecimal.ZERO) < 0) {
            throw new CoreException(ErrorType.BAD_REQUEST, "포인트는 0 미만의 값으로 생성이 불가능합니다.");
        }
        return new PointModel(userUid, point);
    }

    public void changePoint(BigDecimal point) {
        if (point.compareTo(BigDecimal.ZERO) <= 0) {
            throw new CoreException(ErrorType.BAD_REQUEST, "포인트는 0 이하로 충전이 불가능합니다.");
        }
        this.point = this.point.add(point);
    }


    public void deduct(BigDecimal totalAmount) {
        if (totalAmount.doubleValue() <= 0) {
            throw new CoreException(ErrorType.BAD_REQUEST, "결제할 포인트는 음수가 될 수 없습니다.");
        }
        if (this.point.compareTo(totalAmount) < 0) {
            throw new CoreException(ErrorType.BAD_REQUEST, "포인트가 부족합니다.");
        }
        this.point = this.point.subtract(totalAmount);
    }
}
