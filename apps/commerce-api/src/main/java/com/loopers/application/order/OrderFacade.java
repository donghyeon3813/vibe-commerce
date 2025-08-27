package com.loopers.application.order;

import com.loopers.application.issue.listener.CouponIssueEvent;
import com.loopers.application.payment.PaymentProcessorFactory;
import com.loopers.domain.coupon.Coupon;
import com.loopers.domain.coupon.CouponService;
import com.loopers.domain.issue.CouponIssue;
import com.loopers.domain.issue.CouponIssueService;
import com.loopers.domain.order.OrderModel;
import com.loopers.domain.order.OrderService;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductService;
import com.loopers.domain.user.UserModel;
import com.loopers.domain.user.UserService;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderFacade {

    private final UserService userService;
    private final OrderService orderService;
    private final PaymentProcessorFactory paymentProcessorFactory;
    private final OrderProcessor orderProcessor;

    public OrderInfo.OrderResponse order(OrderCommand.Order order) {
        // 유저 확인
        UserModel user = userService.getUser(order.getUserId());
        if (user == null) {
            throw new CoreException(ErrorType.NOT_FOUND, "회원을 찾을 수 없습니다.");
        }
        // 주문 진행
        OrderModel orderModel = orderProcessor.orderProcess(order, user);

        // 결제 진행
        paymentProcessorFactory.getPaymentProcessor(order).process(orderModel, order, user);

        return OrderInfo.OrderResponse.of(orderModel.getId());

    }

    @Transactional(readOnly = true)
    public OrderInfo.OrderDataList getOrders(OrderCommand.GetOrders order) {
        // 유저 확인
        UserModel user = userService.getUser(order.userId());
        if (user == null) {
            throw new CoreException(ErrorType.NOT_FOUND, "회원을 찾을 수 없습니다.");
        }
        List<OrderModel> orders = orderService.getOrders(user.getId());
        if (orders.isEmpty()) {
            return OrderInfo.OrderDataList.of(Collections.emptyList());
        }

        return OrderInfo.OrderDataList.of(orders);
    }

    @Transactional(readOnly = true)
    public OrderInfo.OrderData getOrder(OrderCommand.GetOrder order) {
        // 유저 확인
        UserModel user = userService.getUser(order.userId());
        if (user == null) {
            throw new CoreException(ErrorType.NOT_FOUND, "회원을 찾을 수 없습니다.");
        }
        Optional<OrderModel> orderModel = orderService.getOrder(user.getId(), order.orderId());
        if(orderModel.isEmpty()) {
            throw new CoreException(ErrorType.BAD_REQUEST, "주문 정보를 찾을 수 없습니다.");
        }

        return OrderInfo.OrderData.of(orderModel.get());
    }
}
