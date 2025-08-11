package com.loopers.domain.coupon;

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

public class CouponModelTest {

    @DisplayName("쿠폰이 생성될 떄")
    @Nested
    class Create {
        @DisplayName("value가 0이하이면 BadRequest를 발생한다.")
        @ParameterizedTest
        @ValueSource(ints = {0, -1})
        void throwsBadRequest_whenValueIsNegative(int value) {

            CoreException exception = assertThrows(CoreException.class, () -> Coupon.create(CouponType.FIXED, BigDecimal.valueOf(value)));

            assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);

        }

        @DisplayName("쿠폰 타입이 없으면 BadRequest를 반환한다.")
        @Test
        void throwsBadRequest_whenTypeIsNull() {

            CoreException exception = assertThrows(CoreException.class, () -> Coupon.create(null, BigDecimal.valueOf(1000)));

            assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }

        @DisplayName("정률 쿠폰 타입일때 할인율이 80초과하면 BadRequest를 반환한다.")
        @Test
        void throwsBadRequest_whenValueExceeds80() {

            CoreException exception = assertThrows(CoreException.class, () -> Coupon.create(CouponType.PERCENTAGE, BigDecimal.valueOf(1000)));

            assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }

        @DisplayName("정상 데이터를 입력하면 성공한다.")
        @Test
        void succeeds_whenValidCouponDataIsProvided() {

            Coupon coupon = Coupon.create(CouponType.FIXED, BigDecimal.valueOf(1000));

            assertThat(coupon.getValue().doubleValue()).isEqualTo(1000);
        }
    }
    @DisplayName("할인 값을 계산할때")
    @Nested
    class Calculate {
        @DisplayName("금액이 null일 경우 실패한다.")
        @Test
        void throwsBadRequest_whenTotalAmountIsNull() {

            Coupon coupon = Coupon.create(CouponType.FIXED, BigDecimal.valueOf(1000));

            CoreException exception = assertThrows(CoreException.class, () -> coupon.calculateDiscount(null));

            assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);

        }
        @Test
        void returnsDiscountAmount_whenValidTotalAmountProvided() {
            // given

            Coupon coupon = Coupon.create(CouponType.FIXED, BigDecimal.valueOf(1000));

            // when
            BigDecimal discount = coupon.calculateDiscount(BigDecimal.valueOf(10000));

            // then
            assertThat(discount.doubleValue()).isEqualTo(9000); //
        }
    }
}
