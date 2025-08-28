package com.loopers.application.payment;

import com.loopers.application.order.OrderCommand;
import com.loopers.application.order.listener.OrderEvent;
import com.loopers.domain.order.OrderModel;
import com.loopers.domain.payment.PayType;
import com.loopers.domain.payment.Payment;
import com.loopers.domain.payment.PaymentService;
import com.loopers.domain.point.PointModel;
import com.loopers.domain.point.PointService;
import com.loopers.domain.user.UserModel;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PointProcessor implements PaymentProcessor {
    private final PointService pointService;
    private final PaymentService paymentService;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional(noRollbackFor = CoreException.class)
    public void process(OrderModel orderModel, OrderCommand.Order order, UserModel user) {
        Payment payment = paymentService.createPayment(orderModel.getId(), PayType.POINT);
        //point 조회
        PointModel pointModel = pointService.getPointInfoForUpdate(user.getId())
                .orElseThrow(() -> new CoreException(ErrorType.BAD_REQUEST, "포인트 정보가 잘못되었습니다."));
        try {
            // point 차감
            pointModel.deduct(orderModel.getAmount());
            payment.success();
            eventPublisher.publishEvent(OrderEvent.StatusUpdateEvent.create(orderModel.getId(), "SUCCESS"));
        }catch (CoreException e) {
            payment.fail();
            eventPublisher.publishEvent(OrderEvent.StatusUpdateEvent.create(orderModel.getId(), "FAILED"));
            throw e;
        }

    }

    @Override
    public boolean check(OrderCommand.Order order) {
        return order.getPayment() == OrderCommand.Order.Payment.POINT;
    }
}
