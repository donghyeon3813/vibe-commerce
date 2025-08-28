package com.loopers.application.order;

import com.loopers.application.issue.listener.CouponIssueEvent;
import com.loopers.domain.coupon.Coupon;
import com.loopers.domain.coupon.CouponService;
import com.loopers.domain.issue.CouponIssue;
import com.loopers.domain.issue.CouponIssueService;
import com.loopers.domain.order.OrderModel;
import com.loopers.domain.order.OrderService;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductService;
import com.loopers.domain.user.UserModel;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderProcessor {
    private final CouponIssueService couponIssueService;
    private final CouponService couponService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final OrderService orderService;
    private final ProductService productService;

    @Transactional
    public OrderModel orderProcess(OrderCommand.Order order, UserModel user) {
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
        return orderModel;
    }
}
