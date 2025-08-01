package com.loopers.domain.order;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderItemTest {

    @DisplayName("OrderItem이 생성될 때")
    @Nested
    class Create {
        @DisplayName("productUid가 없으면 BadRequest를 반환한다.")
        @Test
        void throw_exception_when_productUid_is_null() {
            Long productUid = null;
            int quantity = 2;

            CoreException exception = Assertions.assertThrows(CoreException.class, () -> OrderItem.create(productUid, quantity));

            assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }

        @DisplayName("quantity는 0이하 이면 BadRequest를 반환한다.")
        @ParameterizedTest
        @ValueSource(ints = {0, -1})
        void throw_exception_when_quantity_is_zero_or_less(int quantity) {

            Long productUid = 1L;

            CoreException exception = Assertions.assertThrows(CoreException.class, () -> OrderItem.create(productUid, quantity));

            assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);

        }

        @DisplayName("알맞은 데이터를 넣으면 성공한다.")
        @Test
        void success() {

            Long productUid = 1L;
            int quantity = 2;

            OrderItem orderItem = OrderItem.create(productUid, quantity);

            assertThat(orderItem.getProductUid()).isEqualTo(productUid);
            assertThat(orderItem.getQuantity()).isEqualTo(quantity);

        }
    }
}
