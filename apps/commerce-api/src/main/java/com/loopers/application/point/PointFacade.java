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

@Component
@RequiredArgsConstructor
public class PointFacade {
    private final PointService pointService;
    private final UserService userService;

    public PointInfo getPointInfo(PointV1Dto.PointInfoRequest pointInfoRequest) {

        UserModel user = userService.getUser(pointInfoRequest.userId());
        if (user == null) {
            throw new CoreException(ErrorType.BAD_REQUEST, "회원정보가 존재하지 않습니다.");
        }
        PointModel pointModel = pointService.getPointInfo(user.getId());

        return PointInfo.from(pointModel);
    }
}
