package com.loopers.application.point;

import com.loopers.domain.point.PointModel;
import com.loopers.domain.point.PointService;
import com.loopers.domain.user.UserModel;
import com.loopers.domain.user.UserService;
import com.loopers.interfaces.api.point.PointV1Dto;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PointFacade {
    private final PointService pointService;
    private final UserService userService;

    @Transactional(readOnly = true)
    public PointInfo getPointInfo(PointV1Dto.PointInfoRequest pointInfoRequest) {

        UserModel user = userService.getUser(pointInfoRequest.userId());
        if (user == null) {
            return null;
        }
        Optional<PointModel> pointModel = pointService.getPointInfo(user.getId());
        return pointModel.map(PointInfo::from).orElse(null);

    }

    @Transactional
    public PointChargeInfo chargePoint(PointV1Dto.PointChargeRequest pointChargeRequest) {
        UserModel user = userService.getUser(pointChargeRequest.userId());
        if (user == null) {
            throw new CoreException(ErrorType.NOT_FOUND, "회원정보가 존재하지 않습니다.");
        }
        PointModel pointModel = pointService.getPointInfo(user.getId())
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "포인트 정보가 존재하지 않습니다."));
        if (pointModel == null) {
            pointModel = pointService.createPoint(PointCreateInfo.of(user.getId(), pointChargeRequest.point()));
        } else {
            pointModel.changePoint(pointChargeRequest.point());
        }
        return PointChargeInfo.from(pointModel);
    }
}
