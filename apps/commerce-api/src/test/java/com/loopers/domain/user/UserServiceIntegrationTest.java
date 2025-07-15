package com.loopers.domain.user;

import com.loopers.infrastructure.user.UserJpaRepository;
import com.loopers.interfaces.api.user.UserV1Dto;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UserServiceIntegrationTest {
    @Autowired
    private UserService userService;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @DisplayName("회원가입을 수행할 때")
    @Nested
    class Create {
        @DisplayName("이미 존재하는 Id가 있으면 가입을 실패한다.")
        @Test
        void createUser_whenInvalidIdIsProvided() {
            UserModel userModel = UserModel.CreateUser("test1", "test@test.com", "MALE", "2025-07-12");
            userJpaRepository.save(userModel);
            UserV1Dto.SignupRequest signupRequest = new UserV1Dto.SignupRequest("test1", "test@test.com", "MALE", "2025-07-12");

            CoreException exception = assertThrows(CoreException.class, () -> {
                userService.createUser(signupRequest);
            });

            assertThat(exception.getErrorType()).isEqualTo(ErrorType.CONFLICT);
        }

        @DisplayName("성공하면 데이터를 가져온다.")
        @Test
        void createUser_whenSuccessGetInfoProvided() {
            UserV1Dto.SignupRequest signupRequest = new UserV1Dto.SignupRequest("test1", "test@test.com", "MALE", "2025-07-12");

            UserModel user = userService.createUser(signupRequest);

            assertThat(user.getId()).isNotNull();
        }
    }


}
