package com.loopers.application.user;

import com.loopers.domain.user.UserModel;

public record UserInfo(String id, String email, String gender, String birthday) {
    public static UserInfo create(UserModel user) {
        return new UserInfo(user.getUserId(), user.getEmil(), user.getGender().name(), user.getBirthday().toString());
    }
}
