package com.loopers.application.user;

import com.loopers.domain.user.UserModel;
import com.loopers.domain.user.UserService;
import com.loopers.interfaces.api.user.UserV1Dto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserFacade {
    private final UserService userService;

    public UserInfo createUser(UserV1Dto.SignupRequest signupRequest) {
        UserModel user = userService.createUser(signupRequest);
        return UserInfo.create(user);
    }

}
