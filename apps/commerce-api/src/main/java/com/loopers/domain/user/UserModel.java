package com.loopers.domain.user;


import com.loopers.domain.BaseEntity;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user")
@Getter
public class UserModel extends BaseEntity {

    @Column(unique = true, nullable = false, length = 10)
    private String userId;
    @Column(nullable = false)
    private String email;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;
    @Column(nullable = false)
    private LocalDate birthday;

    @Builder
    protected UserModel(String userId, String email, Gender gender, LocalDate birthday) {
        this.userId = userId;
        this.email = email;
        this.gender = gender;
        this.birthday = birthday;
    }

    // user 생성
    public static UserModel CreateUser(String id, String email, String gender, String birthDayStr) {
        validateCreateUser(id, email, birthDayStr);
        LocalDate birthday = LocalDate.parse(birthDayStr);

        return UserModel.builder()
                .userId(id)
                .email(email)
                .gender(Gender.getGender(gender))
                .birthday(birthday)
                .build();
    }

    // user 생성시 validate 검사
    private static void validateCreateUser(String id, String email, String birthDayStr) {
        String idRegex = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z0-9]{1,10}$";
        if (id == null || id.isBlank() || !id.matches(idRegex)) {
            throw new CoreException(ErrorType.BAD_REQUEST, "ID는 영문자 및 숫자 10자 이내여야합니다.");
        }

        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (email == null || email.isBlank() || !email.matches(emailRegex)) {
            throw new CoreException(ErrorType.BAD_REQUEST, "email 형식이 맞지 않습니다.");
        }

        String dateRegex = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$";
        if (birthDayStr == null || birthDayStr.isBlank() || !birthDayStr.matches(dateRegex)) {
            throw new CoreException(ErrorType.BAD_REQUEST, "생년월일 형식이 맞지 않습니다.");
        }

    }

}
