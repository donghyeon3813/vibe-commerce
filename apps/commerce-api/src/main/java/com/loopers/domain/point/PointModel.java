package com.loopers.domain.point;

import com.loopers.domain.BaseEntity;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "point")
public class PointModel extends BaseEntity {
    private Long userUid;
    private int point;
    @Version
    private Long version;

    public PointModel(Long userUid, int point) {
        this.userUid = userUid;
        this.point = point;
    }

    public static PointModel create(long userUid, int point) {
        if (point < 0) {
            throw new CoreException(ErrorType.BAD_REQUEST, "포인트는 0 미만의 값으로 생성이 불가능합니다.");
        }
        return new PointModel(userUid, point);
    }

    public void changePoint(int point) {
        if (point <= 0) {
            throw new CoreException(ErrorType.BAD_REQUEST, "포인트는 0 이하로 충전이 불가능합니다.");
        }
        this.point += point;
    }


    public void deduct(int totalAmount) {
        if (totalAmount <= 0) {
            throw new CoreException(ErrorType.BAD_REQUEST, "결제할 포인트는 음수가 될 수 없습니다.");
        }
        if (this.point < totalAmount) {
            throw new CoreException(ErrorType.BAD_REQUEST, "포인트가 부족합니다.");
        }
        this.point -= totalAmount;
    }
}
