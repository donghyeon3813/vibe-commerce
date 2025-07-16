package com.loopers.domain.point;

import com.loopers.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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

    protected PointModel(Long userUid, int point) {
        this.userUid = userUid;
        this.point = point;
    }

    public static PointModel createPointModel(long userUid, int point) {
        return new PointModel(userUid, point);
    }


}
