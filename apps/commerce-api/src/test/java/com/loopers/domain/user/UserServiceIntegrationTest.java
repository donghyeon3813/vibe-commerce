package com.loopers.domain.user;

import com.loopers.infrastructure.user.UserJpaRepository;
import com.loopers.interfaces.api.user.UserV1Dto;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class UserServiceIntegrationTest {
    @Autowired
    private UserService userService;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Mock
    private UserRepository userRepository;

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

        @DisplayName("성공하면 정보를 저장한다.")
        @Test
        void createUser_whenSuccessGetInfoProvided() {
            UserV1Dto.SignupRequest signupRequest = new UserV1Dto.SignupRequest("test1", "test@test.com", "MALE", "2025-07-12");
            UserService userServiceSpy = spy(new UserService(userRepository));

            userServiceSpy.createUser(signupRequest);

            verify(userServiceSpy).createUser(any());

        }
    }

    @DisplayName("회원 정보를 조회할때")
    @Nested
    class Info {
        @BeforeEach
        void init() {
            UserModel userModel = UserModel.CreateUser("test9999", "test@test.com", Gender.MALE.name(), "2025-07-13");
            userJpaRepository.save(userModel);

        }

        @DisplayName("해당 ID의 회원 정보가 존재할 경우, 회원 정보가 반환된다.")
        @Test
        void returnsUserInfo_whenSuccessProvided() {
            String userId = "test9999";
            String email = "test@test.com";
            Gender gender = Gender.MALE;
            LocalDate birthday = LocalDate.of(2025, 7, 13);

            UserModel userModel = userService.getUser(userId);
            assertAll(
                    () -> assertThat(userModel).isNotNull(),
                    () -> assertThat(userModel.getUserId()).isEqualTo(userId),
                    () -> assertThat(userModel.getEmail()).isEqualTo(email),
                    () -> assertThat(userModel.getGender()).isEqualTo(gender),
                    () -> assertThat(userModel.getBirthday()).isEqualTo(birthday)
            );

            assertThat(userModel.getUserId()).isEqualTo(userId);

        }

        @DisplayName("해당 ID의 회원 정보가 존재하지 않을 경우, NULL을 반환한다.")
        @Test
        void returnsNull_whenInvalidInputProvided() {
            String userId = "test9998";

            UserModel userModel = userService.getUser(userId);

            assertThat(userModel).isNull();

        }
    }


}
