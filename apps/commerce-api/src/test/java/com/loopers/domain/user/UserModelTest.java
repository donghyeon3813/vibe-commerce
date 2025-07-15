package com.loopers.domain.user;


import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;


class UserModelTest {
    @DisplayName("회원가입할 때, ")
    @Nested
    class CreateUser {
        @DisplayName("id, 성별, email, 생년월일을 입력 받으면, 정상적으로 생성된다. ")
        @Test
        void createUserModel_whenIdAndGenderAndEmailAndBirthDayAreProvided() {
            // arrange
            String id = "testId1";
            String gender = "MALE";
            String email = "test@test.com";
            String birthDay = "2025-07-12";

            // act
            UserModel userModel = UserModel.CreateUser(id, email, gender, birthDay);

            // assert
            assertAll(
                    () -> assertThat(userModel.getId()).isNotNull(),
                    () -> assertThat(userModel.getUserId()).isEqualTo(id),
                    () -> assertThat(userModel.getEmail()).isEqualTo(email),
                    () -> assertThat(userModel.getBirthday()).isEqualTo(birthDay),
                    () -> assertThat(userModel.getGender()).isEqualTo(Gender.valueOf(gender))
            );
        }

        @DisplayName("id가 숫자 및 영어 10자 이내이지 않으면 Bad_Request 예외가 발생한다.")
        @ParameterizedTest
        @ValueSource(strings = {"abcdef", "123456", "abc12345678", "a1234567890", "abc!", "", " ", "abc_123"})
        void throwsBadRequestException_whenIdFormatIsInvalid(String id) {
            //  arrange
            String email = "test@test.com";
            String birthDay = "2025-07-12";
            String gender = "MALE";

            // act
            CoreException result = assertThrows(CoreException.class, () -> {
                UserModel.CreateUser(id, email, gender, birthDay);
            });

            // assert
            assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);

        }

        @DisplayName("이메일이 이메일 형식이 아니면 Bad_Request 예외가 발생한다.")
        @ParameterizedTest
        @ValueSource(strings = {
                "plainaddress",
                "@no-local-part.com",
                "user@",
                "user@.com",
                "user@com",
                "user@@domain.com"
        })
        void throwsBadRequestException_whenEmailFormatIsInvalid(String email) {
            //  arrange
            String id = "test1";
            String birthDay = "2025-07-12";
            String gender = "MALE";

            // act
            CoreException result = assertThrows(CoreException.class, () -> {
                UserModel.CreateUser(id, email, gender, birthDay);
            });

            // assert
            assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);

        }

        @DisplayName("생년원일이 yyyy-MM-dd 형식이 아니면 Bad_Request 예외가 발생한다.")
        @ParameterizedTest
        @ValueSource(strings = {
                "1234-12/12",
                "1234/12-12",
                "1234/12/12",
                "123a-12-12",
                "1234-12a-12",
                "1234-12-12a"
        })
        void throwsBadRequestException_whenBirthdayFormatIsInvalid(String birthday) {
            //  arrange
            String id = "test1";
            String email = "test@test.com";
            String gender = "MALE";

            // act
            CoreException result = assertThrows(CoreException.class, () -> {
                UserModel.CreateUser(id, email, gender, birthday);
            });

            // assert
            assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);

        }
    }
}
