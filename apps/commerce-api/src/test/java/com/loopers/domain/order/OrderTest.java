package com.loopers.domain.order;


import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

public class OrderTest {

    @DisplayName("주문이 생성될 떄")
    @Nested
    class Create {
        @DisplayName("주문항목이 없으면 BadRequest를 반환한다.")
        @Test
        void throwsBadRequest_WhenOrderItemIsNull() {
            OrderModel orderModel = OrderModel.create(999L, BigDecimal.valueOf(1000), Address.of("주소", "01000000000", "이름"), null);
            CoreException exception = assertThrows(CoreException.class, () -> orderModel.addOrderItem(null));

            assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);

        }

        @DisplayName("userId가 없으면 BadRequest를 반환한다..")
        @Test
        void throwsBadRequest_WhenUserIdIsNull() {

            CoreException exception = assertThrows(CoreException.class,
                    () -> OrderModel.create(null, BigDecimal.valueOf(1000), Address.of("주소", "01000000000", "이름"), null));

            assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);

        }

        @DisplayName("amount가 0이하이면 BadRequest를 반환한다..")
        @ParameterizedTest
        @ValueSource(ints = {0, -1})
        void throwsBadRequest_WhenAmountIsNegative(int amount) {

            CoreException exception = assertThrows(CoreException.class,
                    () -> OrderModel.create(999L, BigDecimal.valueOf(amount), Address.of("주소", "01000000000", "이름"), null));

            assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);

        }

        @DisplayName("정보가 알맞으면 성공한다.")
        @Test
        void success() {

            OrderModel orderModel = OrderModel.create(999L, BigDecimal.valueOf(100), Address.of("주소", "01000000000", "이름"), null);
            assertThat(orderModel).isNotNull();
            assertThat(orderModel.getUserUid()).isEqualTo(999L);
            assertThat(orderModel.getAmount().doubleValue()).isEqualTo(100);
        }

    }

}
