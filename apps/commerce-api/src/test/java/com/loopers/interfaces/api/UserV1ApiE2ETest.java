package com.loopers.interfaces.api;

import com.loopers.domain.user.UserModel;
import com.loopers.infrastructure.user.UserJpaRepository;
import com.loopers.interfaces.api.user.UserV1Dto;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserV1ApiE2ETest {

    private static final String USER_ENDPOINT = "/api/v1/users";


    private final DatabaseCleanUp databaseCleanUp;
    private final TestRestTemplate testRestTemplate;
    private final UserJpaRepository userJpaRepository;

    @Autowired
    public UserV1ApiE2ETest(DatabaseCleanUp databaseCleanUp, TestRestTemplate testRestTemplate,
                            UserJpaRepository userJpaRepository) {
        this.databaseCleanUp = databaseCleanUp;
        this.testRestTemplate = testRestTemplate;
        this.userJpaRepository = userJpaRepository;
    }

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @DisplayName("POST /api/v1/users")
    @Nested
    class Signup {
        @DisplayName("회원가입이 성공할 경우 생성된 유저 정보를 응답으로 받는다.")
        @Test
        void returnsUserInfo_whenSuccessProvided() {
            String request = """
                    {
                      "id": "test1",
                      "email": "test@test.com",
                      "gender": "MALE",
                      "birthday": "2025-07-13"
                    }
                    """;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(request, headers);
            ParameterizedTypeReference<ApiResponse<UserV1Dto.UserCreateResponse>> responseType = new ParameterizedTypeReference<>() {
            };
            ResponseEntity<ApiResponse<UserV1Dto.UserCreateResponse>> response =
                    testRestTemplate.exchange(USER_ENDPOINT, HttpMethod.POST, entity, responseType);

            assertAll(
                    () -> assertThat(response.getBody().data()).isNotNull(),
                    () -> assertThat(response.getBody().data().id()).isEqualTo("test1"),
                    () -> assertThat(response.getBody().data().email()).isEqualTo("test@test.com"),
                    () -> assertThat(response.getBody().data().gender()).isEqualTo("MALE"),
                    () -> assertThat(response.getBody().data().birthday()).isEqualTo("2025-07-13")
            );
        }

        @DisplayName("회원가입시 성별이 누락되어 있으면 Bad_Request 를 응답 받는다.")
        @Test
        void throwsException_whenInvalidGenderProvided() {
            String request = """
                    {
                      "id": "test2",
                      "email": "test@test.com",
                      "gender": "",
                      "birthday": "2025-07-13"
                    }
                    """;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(request, headers);
            ParameterizedTypeReference<ApiResponse<UserV1Dto.UserCreateResponse>> responseType = new ParameterizedTypeReference<>() {
            };
            ResponseEntity<ApiResponse<UserV1Dto.UserCreateResponse>> response =
                    testRestTemplate.exchange(USER_ENDPOINT, HttpMethod.POST, entity, responseType);

            assertAll(
                    () -> assertTrue(response.getStatusCode().is4xxClientError()),
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST)
            );
        }
    }

    @DisplayName("GET /api/v1/users/me")
    @Nested
    class Info {
        @BeforeEach
        void init() {
            UserModel userModel = UserModel.CreateUser("test9999", "test@test.com", "MALE", "2025-07-13");
            userJpaRepository.save(userModel);

        }

        @DisplayName("회원 정보를 조회할 때 해당 ID의 회원이 존재할 경우, 회원 정보가 반환된다.")
        @Test
        void returnsUserInfo_whenSuccessProvided() {
            String id = "test9999";
            String email = "test@test.com";
            String gender = "MALE";
            String birthday = "2025-07-13";

            String requestUrl = USER_ENDPOINT + "/me";
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-USER-ID", id);

            ParameterizedTypeReference<ApiResponse<UserV1Dto.UserInfoResponse>> responseType = new ParameterizedTypeReference<>() {
            };
            ResponseEntity<ApiResponse<UserV1Dto.UserInfoResponse>> response =
                    testRestTemplate.exchange(requestUrl, HttpMethod.GET, new HttpEntity<>(headers), responseType);

            assertAll(
                    () -> assertThat(response.getBody().data()).isNotNull(),
                    () -> assertThat(response.getBody().data().id()).isEqualTo(id),
                    () -> assertThat(response.getBody().data().email()).isEqualTo(email),
                    () -> assertThat(response.getBody().data().gender()).isEqualTo(gender),
                    () -> assertThat(response.getBody().data().birthday()).isEqualTo(birthday)
            );
        }

        @DisplayName("회원정보를 조회할 때 존재하지 않는 ID 로 조회할 경우 404 Not Found 응답을 반환한다.")
        @Test
        void throwsException_whenInvalidUserIdProvided() {
            String id = "test9998";

            String requestUrl = USER_ENDPOINT + "/me";
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-USER-ID", id);

            ParameterizedTypeReference<ApiResponse<UserV1Dto.UserInfoResponse>> responseType = new ParameterizedTypeReference<>() {
            };
            ResponseEntity<ApiResponse<UserV1Dto.UserInfoResponse>> response =
                    testRestTemplate.exchange(requestUrl, HttpMethod.GET, new HttpEntity<>(headers), responseType);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }
}
