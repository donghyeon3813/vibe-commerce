package com.loopers.application.user;

import com.loopers.application.point.PointCreateInfo;
import com.loopers.domain.point.PointModel;
import com.loopers.domain.point.PointService;
import com.loopers.domain.user.UserModel;
import com.loopers.domain.user.UserService;
import com.loopers.interfaces.api.user.UserV1Dto;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class UserFacade {
    private final UserService userService;
    private final PointService pointService;

    @Transactional
    public UserInfo createUser(UserV1Dto.SignupRequest signupRequest) {
        UserModel user = userService.createUser(signupRequest);
        pointService.createPoint(PointCreateInfo.of(user.getId(), 0));
        return UserInfo.create(user);
    }

    public UserInfo getUser(String userId) {

        if (userId == null) {
            throw new CoreException(ErrorType.NOT_FOUND, "존재하지 않는 유저입니다.");
        }
        UserModel user = userService.getUser(userId);
        if (user == null) {
            throw new CoreException(ErrorType.NOT_FOUND, "존재하지 않는 유저입니다.");
        }
        return UserInfo.create(user);
    }
}
