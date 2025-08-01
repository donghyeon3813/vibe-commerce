package com.loopers.interfaces.api;

import com.loopers.domain.point.PointModel;
import com.loopers.domain.user.Gender;
import com.loopers.domain.user.UserModel;
import com.loopers.infrastructure.point.PointJpaRepository;
import com.loopers.infrastructure.user.UserJpaRepository;
import com.loopers.interfaces.api.point.PointV1Dto;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PointV1ApiE2ETest {

    private static final String POINT_ENDPOINT = "/api/v1/points";

    private final TestRestTemplate testRestTemplate;
    private final DatabaseCleanUp databaseCleanUp;
    private final PointJpaRepository pointJpaRepository;
    private final UserJpaRepository userJpaRepository;

    @Autowired
    public PointV1ApiE2ETest(TestRestTemplate testRestTemplate, DatabaseCleanUp databaseCleanUp,
                             UserJpaRepository userJpaRepository, PointJpaRepository pointJpaRepository) {
        this.testRestTemplate = testRestTemplate;
        this.databaseCleanUp = databaseCleanUp;
        this.userJpaRepository = userJpaRepository;
        this.pointJpaRepository = pointJpaRepository;
    }

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @DisplayName("GET /api/v1/points")
    @Nested
    class Info {

        @BeforeEach
        void init() {
            UserModel userModel = UserModel.CreateUser("test9999", "test@test.com", Gender.MALE.name(), "2025-07-13");
            UserModel saveUser = userJpaRepository.save(userModel);
            PointModel pointModel = PointModel.create(saveUser.getId(), 100);
            pointJpaRepository.save(pointModel);
        }

        @DisplayName("포인트 조회에 성공할 경우, 보유 포인트를 응답으로 반환한다.")
        @Test
        void returnsPointBalance_whenPointInquirySucceeds() {
            String userId = "test9999";

            HttpHeaders headers = new HttpHeaders();
            headers.set("X-USER-ID", userId);

            ParameterizedTypeReference<ApiResponse<PointV1Dto.PointInfoResponse>> responseType = new ParameterizedTypeReference<>() {
            };
            ResponseEntity<ApiResponse<PointV1Dto.PointInfoResponse>> response =
                    testRestTemplate.exchange(POINT_ENDPOINT, HttpMethod.GET, new HttpEntity<>(headers), responseType);

            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                    () -> assertThat(response.getBody().data().point()).isEqualTo(100)
            );

        }

        @DisplayName("X-USER-ID 헤더가 없을 경우, 400 Bad Request 응답을 반환한다.")
        @Test
        void throwsException_whenInvalidUserId() {
            ParameterizedTypeReference<ApiResponse<PointV1Dto.PointInfoResponse>> responseType = new ParameterizedTypeReference<>() {
            };

            ResponseEntity<ApiResponse<PointV1Dto.PointInfoResponse>> response =
                    testRestTemplate.exchange(POINT_ENDPOINT, HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), responseType);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }
    }

    @DisplayName("POST /api/v1/points/charge")
    @Nested
    class Charge {
        @BeforeEach
        void init() {
            UserModel userModel = UserModel.CreateUser("charge9999", "test@test.com", Gender.MALE.name(), "2025-07-13");
            UserModel saveUser = userJpaRepository.save(userModel);
            PointModel pointModel = PointModel.create(saveUser.getId(), 100);
            pointJpaRepository.save(pointModel);
        }

        @DisplayName("존재하는 유저가 1000월을 충전할 경우, 충전된 보유 총량을 응답으로 반환한다.")
        @Test
        void returnsPointCharges_whenPointInquirySucceeds() {
            String userId = "charge9999";
            int point = 100;
            int chargePoint = 1000;
            String request = """
                    {
                      "chargePoint": 1000
                    }
                    """;
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-USER-ID", userId);
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> httpEntity = new HttpEntity<>(request, headers);
            String requestUrl = POINT_ENDPOINT + "/charge";

            ParameterizedTypeReference<ApiResponse<PointV1Dto.PointChargeResponse>> responseType = new ParameterizedTypeReference<>() {
            };
            ResponseEntity<ApiResponse<PointV1Dto.PointChargeResponse>> response =
                    testRestTemplate.exchange(requestUrl, HttpMethod.POST, httpEntity, responseType);
            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                    () -> assertThat(response.getBody().data().point()).isEqualTo(point + chargePoint)
            );
        }

        @DisplayName("존재하지 않는 유저로 요청할 경우, 404 Not Found 응답을 반환한다.")
        @Test
        void throwsException_whenUserNotFound() {
            String userId = "charge9998";
            String request = """
                    {
                      "chargePoint": 1000
                    }
                    """;
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-USER-ID", userId);
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> httpEntity = new HttpEntity<>(request, headers);
            String requestUrl = POINT_ENDPOINT + "/charge";

            ParameterizedTypeReference<ApiResponse<PointV1Dto.PointChargeResponse>> responseType = new ParameterizedTypeReference<>() {
            };
            ResponseEntity<ApiResponse<PointV1Dto.PointChargeResponse>> response =
                    testRestTemplate.exchange(requestUrl, HttpMethod.POST, httpEntity, responseType);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

}
