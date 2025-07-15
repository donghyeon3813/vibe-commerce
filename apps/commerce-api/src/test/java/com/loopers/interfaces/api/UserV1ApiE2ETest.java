package com.loopers.interfaces.api;

import com.loopers.interfaces.api.user.UserV1Dto;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
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

    @Autowired
    public UserV1ApiE2ETest(DatabaseCleanUp databaseCleanUp, TestRestTemplate testRestTemplate) {
        this.databaseCleanUp = databaseCleanUp;
        this.testRestTemplate = testRestTemplate;
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
}
