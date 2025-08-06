package com.loopers.application;

import com.loopers.application.issue.CouponIssueCommand;
import com.loopers.application.issue.CouponIssueFacade;
import com.loopers.application.issue.CouponIssueInfo;
import com.loopers.application.order.OrderCommand;
import com.loopers.domain.coupon.Coupon;
import com.loopers.domain.coupon.CouponRepository;
import com.loopers.domain.coupon.CouponType;
import com.loopers.domain.issue.CouponIssue;
import com.loopers.domain.issue.CouponIssueRepository;
import com.loopers.domain.user.Gender;
import com.loopers.domain.user.UserModel;
import com.loopers.infrastructure.coupon.CouponJpaRepository;
import com.loopers.infrastructure.issue.CouponIssueJpaRepository;
import com.loopers.infrastructure.user.UserJpaRepository;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class CouponIssueIntegrationTest {
    @Autowired
    private CouponIssueFacade CouponIssueFacade;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private CouponIssueJpaRepository couponIssueRepository;

    @Autowired
    private CouponJpaRepository couponJpaRepository;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @DisplayName("쿠폰 목록을 조회할 때")
    @Nested
    class infos {
        @DisplayName("등록되지 않은 유저면 NotFound를 반환한다.")
        @Test
        void throwsNotFound_whenUserNotFound() {
            String userId = "notFound";

            CouponIssueCommand.GetList getList = CouponIssueCommand.GetList.of(userId);

            CoreException exception = assertThrows(CoreException.class, () -> CouponIssueFacade.getList(getList));

            assertThat(exception.getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
        }
        @DisplayName("등록된 쿠폰이 없으면 빈 목록을 반환한다.")
        @Test
        void returnsEmptyList_whenCouponNotFound() {
            UserModel saveUser = userJpaRepository
                    .save(UserModel.CreateUser("test9998", "test@test.com", Gender.MALE.name(), "2025-07-13"));

            CouponIssueCommand.GetList getList = CouponIssueCommand.GetList.of(saveUser.getUserId());
            CouponIssueInfo.ListInfo listInfo = CouponIssueFacade.getList(getList);

            assertThat(listInfo.list()).hasSize(0);
        }
        @DisplayName("등록된 쿠폰목록을 반환한다.")
        @Test
        void returnsCouponList_whenSucessful() {
            UserModel saveUser = userJpaRepository
                    .save(UserModel.CreateUser("test9998", "test@test.com", Gender.MALE.name(), "2025-07-13"));
            Coupon savedCoupon = couponJpaRepository.save(Coupon.create(CouponType.FIXED_AMOUNT, BigDecimal.valueOf(500)));
            couponIssueRepository.save(CouponIssue.of(savedCoupon.getId(), saveUser.getId()));

            CouponIssueCommand.GetList getList = CouponIssueCommand.GetList.of(saveUser.getUserId());
            CouponIssueInfo.ListInfo listInfo = CouponIssueFacade.getList(getList);

            assertThat(listInfo.list()).hasSize(1);
            assertThat(listInfo.list().get(0).getValue().doubleValue()).isEqualTo(savedCoupon.getValue().doubleValue());
            assertThat(listInfo.list().get(0).getCouponType()).isEqualTo(savedCoupon.getCouponType());
        }
    }

}
