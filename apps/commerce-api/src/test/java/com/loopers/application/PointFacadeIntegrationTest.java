package com.loopers.application;

import com.loopers.application.point.PointChargeInfo;
import com.loopers.application.point.PointFacade;
import com.loopers.application.point.PointInfo;
import com.loopers.domain.point.PointModel;
import com.loopers.domain.user.Gender;
import com.loopers.domain.user.UserModel;
import com.loopers.infrastructure.point.PointJpaRepository;
import com.loopers.infrastructure.user.UserJpaRepository;
import com.loopers.interfaces.api.point.PointV1Dto;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class PointFacadeIntegrationTest {
    @Autowired
    private PointFacade pointFacade;
    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private PointJpaRepository pointJpaRepository;


    @DisplayName("포인트를 충전할 때")
    @Nested
    class Charge {

        @DisplayName("존재하지 않는 유저 ID 로 충전 시도한 경우, 실패한다.")
        @Test
        void throwsException_whenUserIdInvalid() {
            PointV1Dto.PointChargeRequest pointChargeRequest = PointV1Dto.PointChargeRequest.of(100, "charge5000");

            CoreException result = assertThrows(CoreException.class, () -> pointFacade.chargePoint(pointChargeRequest));

            assertThat(result.getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
        }

        @DisplayName("존재하는 유저 ID 로 충전 시도한 경우, 성공한다.")
        @Test
        void returnsPointInfo_whenUserExistsAndChargesSuccessfully() {
            PointV1Dto.PointChargeRequest pointChargeRequest = PointV1Dto.PointChargeRequest.of(100, "test9998");
            UserModel userModel = UserModel.CreateUser("test9998", "test@test.com", Gender.MALE.name(), "2025-07-13");
            UserModel saveUser = userJpaRepository.save(userModel);
            PointModel pointModel = PointModel.create(saveUser.getId(), BigDecimal.valueOf(1000));
            pointJpaRepository.save(pointModel);

            PointChargeInfo pointInfo = pointFacade.chargePoint(pointChargeRequest);

            assertThat(pointInfo).isNotNull();
            assertThat(pointInfo.getPoint().doubleValue()).isEqualTo(1100);
        }

    }
    @DisplayName("포인트 정보를 조회할 때")
    @Nested
    class Info {

        @DisplayName("해당 아이디의 회원이 존재할 경우 포인트가 반환된다.")
        @Test
        void returnsPointInfo_whenUserExists() {
            UserModel userModel = UserModel.CreateUser("info3000", "test@test.com", "MALE", "2025-07-13");
            UserModel user = userJpaRepository.save(userModel);
            PointModel pointModel = new PointModel(user.getId(), BigDecimal.valueOf(100));
            pointJpaRepository.save(pointModel);
            String userId = "info3000";
            PointV1Dto.PointInfoRequest pointInfoRequest = PointV1Dto.PointInfoRequest.of(userId);
            PointInfo pointInfo = pointFacade.getPointInfo(pointInfoRequest);

            assertThat(pointInfo).isNotNull();
            assertThat(pointInfo.point().doubleValue()).isEqualTo(100);

        }

        @DisplayName("해당 아이디의 회원이 존재하지 않을 경우 Null 이 반환된다.")
        @Test
        void returnsNull_whenUserDoesNotExist() {
            String userId = "info3001";
            PointV1Dto.PointInfoRequest pointInfoRequest = PointV1Dto.PointInfoRequest.of(userId);
            PointInfo pointInfo = pointFacade.getPointInfo(pointInfoRequest);

            assertThat(pointInfo).isNull();
        }

    }
}
