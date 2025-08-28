package com.loopers.application.issue.listener;

import com.loopers.domain.issue.CouponIssue;
import com.loopers.domain.issue.CouponIssueService;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class CouponIssueListener {
    private final CouponIssueService couponIssueService;


    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleUseCouponIssue(CouponIssueEvent.UseCouponIssueEvent event) {
        log.info("handleUseCouponIssue {}", event);
        CouponIssue couponIssue = couponIssueService
                .findById(event.couponIssueId)
                .orElseThrow(() -> new CoreException(ErrorType.BAD_REQUEST, "사용할 수 없는 쿠폰입니다."));
        couponIssue.use();
    }
}
