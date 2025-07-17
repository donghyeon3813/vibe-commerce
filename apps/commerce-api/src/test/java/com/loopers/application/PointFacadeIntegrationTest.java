package com.loopers.application;

import com.loopers.application.point.PointChargeInfo;
import com.loopers.application.point.PointFacade;
import com.loopers.domain.point.PointModel;
import com.loopers.domain.user.Gender;
import com.loopers.domain.user.UserModel;
import com.loopers.infrastructure.point.PointJpaRepostiroy;
import com.loopers.infrastructure.user.UserJpaRepository;
import com.loopers.interfaces.api.point.PointV1Dto;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class PointFacadeIntegrationTest {
    @Autowired
    private PointFacade pointFacade;
    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private PointJpaRepostiroy pointJpaRepostiroy;


    @DisplayName("포인트를 충전할 때")
    @Nested
    class Charge {

        @DisplayName("존재하지 않는 유저 ID 로 충전 시도한 경우, 실패한다.")
        @Test
        void throwsException_whenUserIdInvalid() {
            PointV1Dto.PointChargeRequest pointChargeRequest = PointV1Dto.PointChargeRequest.of(100, "test9999");

            CoreException result = assertThrows(CoreException.class, () -> pointFacade.chargePoint(pointChargeRequest));

            assertThat(result.getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
        }

        @DisplayName("존재하는 유저 ID 로 충전 시도한 경우, 성공한다.")
        @Test
        void returnsPointInfo_whenUserExistsAndChargesSuccessfully() {
            PointV1Dto.PointChargeRequest pointChargeRequest = PointV1Dto.PointChargeRequest.of(100, "test9998");
            UserModel userModel = UserModel.CreateUser("test9998", "test@test.com", Gender.MALE.name(), "2025-07-13");
            UserModel saveUser = userJpaRepository.save(userModel);
            PointModel pointModel = PointModel.createPointModel(saveUser.getId(), 1000);
            pointJpaRepostiroy.save(pointModel);

            PointChargeInfo pointInfo = pointFacade.chargePoint(pointChargeRequest);

            assertThat(pointInfo).isNotNull();
            assertThat(pointInfo.getPoint()).isEqualTo(1100);
        }

    }
}
