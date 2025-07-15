package com.loopers.interfaces.api.user;

import com.loopers.interfaces.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "User V1 API", description = "회원 정보 API 입니다.")
public interface UserV1ApiSpec {
    @Operation(
            summary = "회원가입",
            description = "id, email, 성별, 생일을 입력하여 회원 정보를 저장합니다."
    )
    ApiResponse<UserV1Dto.UserCreateResponse> createUser(
            @Schema(name = "id: test1, 이메일:test@test.com, 성별: MAIL, 생일:2025-07-12")
            UserV1Dto.SignupRequest signupRequest
    );
}
