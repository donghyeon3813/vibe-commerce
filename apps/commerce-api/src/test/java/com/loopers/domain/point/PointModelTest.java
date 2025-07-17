package com.loopers.domain.point;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

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
            PointModel pointModel = PointModel.createPointModel(1L, 100);

            CoreException result = assertThrows(CoreException.class, () -> pointModel.changePoint(point));

            assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);

        }
    }
}
