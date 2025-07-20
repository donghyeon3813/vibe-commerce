package com.loopers.interfaces.api.user;

import com.loopers.application.user.UserFacade;
import com.loopers.application.user.UserInfo;
import com.loopers.interfaces.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.loopers.support.error.HeaderConstants.X_USER_ID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserV1Controller implements UserV1ApiSpec {

    private final UserFacade userFacade;

    @PostMapping
    @Override
    public ApiResponse<UserV1Dto.UserCreateResponse> createUser(@RequestBody @Validated UserV1Dto.SignupRequest signupRequest) {
        UserInfo userInfo = userFacade.createUser(signupRequest);
        UserV1Dto.UserCreateResponse response = UserV1Dto.UserCreateResponse.from(userInfo);
        return ApiResponse.success(response);
    }

    @GetMapping("/me")
    @Override
    public ApiResponse<UserV1Dto.UserInfoResponse> getUserInfo(@RequestHeader(value = X_USER_ID, required = false) String userId) {
        UserInfo userInfo = userFacade.getUser(userId);
        UserV1Dto.UserInfoResponse response = UserV1Dto.UserInfoResponse.from(userInfo);
        return ApiResponse.success(response);
    }


}
