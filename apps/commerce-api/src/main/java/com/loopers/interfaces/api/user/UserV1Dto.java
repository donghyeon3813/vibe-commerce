package com.loopers.interfaces.api.user;

import com.loopers.application.user.UserInfo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

public class UserV1Dto {
    public record UserCreateResponse(String id, String email, String gender, String birthday) {
        public static UserCreateResponse from(UserInfo info) {
            return new UserCreateResponse(info.id(), info.email(), info.gender(), info.birthday());
        }
    }

    @Getter
    public static class SignupRequest {
        private String id;
        private String email;
        @Pattern(regexp = "MALE|FEMALE", message = "성별은 MALE 또는 FEMALE 중 하나여야 합니다.")
        @NotBlank
        private String gender;
        private String birthday;

        public SignupRequest(String id, String email, String gender, String birthday) {
            this.id = id;
            this.email = email;
            this.gender = gender;
            this.birthday = birthday;
        }
    }

    public record UserInfoResponse(String id, String email, String gender, String birthday) {
        public static UserInfoResponse from(UserInfo info) {
            return new UserInfoResponse(info.id(), info.email(), info.gender(), info.birthday());
        }
    }
}
