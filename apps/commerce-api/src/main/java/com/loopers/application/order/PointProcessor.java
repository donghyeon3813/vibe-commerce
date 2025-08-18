package com.loopers.application.order;

import com.loopers.domain.order.OrderModel;
import com.loopers.domain.payment.PaymentService;
import com.loopers.domain.point.PointModel;
import com.loopers.domain.point.PointService;
import com.loopers.domain.user.UserModel;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PointProcessor implements PaymentProcessor {
    private final PointService pointService;
    private final PaymentService paymentService;
    @Override
    public void process(OrderModel orderModel, OrderCommand.Order order, UserModel user) {
        //point 조회
        PointModel pointModel = pointService.getPointInfoForUpdate(user.getId())
                .orElseThrow(() -> new CoreException(ErrorType.BAD_REQUEST, "포인트 정보가 잘못되었습니다."));
        // point 차감
        pointModel.deduct(orderModel.getAmount());
        // 결제 생성
        paymentService.createPayment(orderModel.getId());
    }

    @Override
    public boolean check(OrderCommand.Order order) {
        return order.getPayment() == OrderCommand.Order.Payment.POINT;
    }
}
