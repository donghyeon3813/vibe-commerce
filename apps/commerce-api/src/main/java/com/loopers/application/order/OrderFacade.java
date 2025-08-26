package com.loopers.application.order;

import com.loopers.application.issue.listener.CouponIssueEvent;
import com.loopers.domain.coupon.Coupon;
import com.loopers.domain.coupon.CouponService;
import com.loopers.domain.issue.CouponIssue;
import com.loopers.domain.issue.CouponIssueService;
import com.loopers.domain.order.OrderModel;
import com.loopers.domain.order.OrderService;
import com.loopers.domain.payment.PaymentService;
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
    private final ProductService productService;

    private final OrderService orderService;
    private final CouponIssueService couponIssueService;
    private final CouponService couponService;

    private final PaymentProcessorFactory paymentProcessorFactory;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public OrderInfo.OrderResponse order(OrderCommand.Order order) {
        // 유저 확인
        UserModel user = userService.getUser(order.getUserId());
        if (user == null) {
            throw new CoreException(ErrorType.NOT_FOUND, "회원을 찾을 수 없습니다.");
        }
        // product uid 추출
        List<OrderCommand.Order.OrderItem> items = order.getItems();
        Set<Long> productUidList = items.stream().map(OrderCommand.Order.OrderItem::getProductId).collect(Collectors.toSet());

        //product 조회
        List<Product> productList = productService.getProductsByProductUidsForUpdate(productUidList);
        productService.checkProductConsistency(productUidList.size(), productList.size());

        Coupon coupon = null;
        Optional<CouponIssue> couponIssue;
        Long couponId = 0L;
        if (order.getCouponId() != null) {
            couponIssue = Optional.ofNullable(couponIssueService.findByIdAndUseFlagForUpdate(order.getCouponId())
                    .orElseThrow(() -> new CoreException(ErrorType.BAD_REQUEST, "사용할 수 없는 쿠폰입니다.")));
            coupon = couponService.getCoupon(couponIssue.get().getCouponUid());
            couponId = couponIssue.get().getId();
            applicationEventPublisher.publishEvent(CouponIssueEvent.UseCouponIssueEvent.of(couponId));
        }
        //totalAmount 계산
        BigDecimal totalAmount = orderService.calulateTotalAmount(items, productList, coupon);

        //order 생성
        OrderModel orderModel = orderService
                .create(user.getId(), order.getItems(), totalAmount, order.getPhone(), order.getReceiverName(), order.getAddress(), couponId);

        // 재고 차감
        productService.deductQuantity(items, productList);

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
