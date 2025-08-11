package com.loopers.domain.point;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PointModelTest {

    @DisplayName("포인트를 충전할 때")
    @Nested
    class Charge {
        @DisplayName("0 이하 정수로 포인트를 충전 시 실패한다.")
        @ParameterizedTest
        @ValueSource(ints = {0, -1})
        void throwsException_whenPointInvalid(int point) {

            PointModel pointModel = PointModel.create(1L, BigDecimal.valueOf(1000));

            CoreException result = assertThrows(CoreException.class, () -> pointModel.changePoint(BigDecimal.valueOf(point)));

            assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);

        }
    }

    @DisplayName("포인트를 차감할 때")
    @Nested
    class Deduct {
        @DisplayName("0 이하 정수로 포인트를 차감 시 실패한다.")
        @ParameterizedTest
        @ValueSource(ints = {0, -1})
        void throwsException_whenPointInvalid(int point) {
            PointModel pointModel = PointModel.create(1L, BigDecimal.valueOf(100));

            CoreException result = assertThrows(CoreException.class, () -> pointModel.deduct(BigDecimal.valueOf(point)));

            assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);

        }

        @DisplayName("포인트가 부족하면 차감에 실패한다.")
        @ParameterizedTest
        @ValueSource(ints = {500})
        void throwsException_whenPointLack(int point) {
            PointModel pointModel = PointModel.create(1L, BigDecimal.valueOf(100));

            CoreException result = assertThrows(CoreException.class, () -> pointModel.deduct(BigDecimal.valueOf(point)));

            assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);

        }

        @DisplayName("포인트가 부족하지 않으면 차감에 성공한다..")
        @ParameterizedTest
        @ValueSource(ints = {500})
        void deduct_when_sufficient(int point) {
            PointModel pointModel = PointModel.create(1L, BigDecimal.valueOf(1000));

            pointModel.deduct(BigDecimal.valueOf(point));

            assertThat(pointModel.getPoint().doubleValue()).isEqualTo(1000 - point);

        }
    }
}
