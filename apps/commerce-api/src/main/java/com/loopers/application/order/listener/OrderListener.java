package com.loopers.application.order.listener;

import com.loopers.application.common.event.MetricsEvent;
import com.loopers.application.delivery.listener.DeliveryEvent;
import com.loopers.domain.issue.CouponIssue;
import com.loopers.domain.issue.CouponIssueService;
import com.loopers.domain.order.OrderModel;
import com.loopers.domain.order.OrderService;
import com.loopers.domain.product.ProductService;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderListener {
    private final OrderService orderService;
    private final ProductService productService;
    private final CouponIssueService couponIssueService;
    private final ApplicationEventPublisher eventPublisher;
    private final KafkaTemplate<Object, Object> kafkaTemplate;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void orderUpdate(OrderEvent.StatusUpdateEvent event) {
        log.info("Order update event: {}", event);
        OrderModel order = orderService.getOrder(event.getId())
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "해당 하는 주문 정보가 없습니다."));
        if("SUCCESS".equals(event.getStatus())){
            success(order);
        }else {
            fail(order);
        }

    }
    private void fail(OrderModel order) {
        log.info("Order fail event: {}", order);
        order.getItems().forEach(item -> productService.getProductInfo(item.getProductUid())
                .ifPresent(product -> product.restoreStock(item.getQuantity())));
        order.changeStatusToFailed();
        if(order.getCouponUid() != null) {
            Optional<CouponIssue> couponIssue = couponIssueService.findById(order.getCouponUid());
            couponIssue.ifPresent(CouponIssue::revoke);
        }
        eventPublisher.publishEvent(DeliveryEvent.OrderResultEvent
                .of(order.getUserUid(), order.getAddress(), order.getOrderStatus(),
                        order.getAmount(), order.getCouponUid(), order.getItems()));
    }
    private void success(OrderModel order) {
        log.info("Order success event: {}", order);
        order.changeStatusToPaid();
        eventPublisher.publishEvent(DeliveryEvent.OrderResultEvent
                .of(order.getUserUid(), order.getAddress(), order.getOrderStatus(),
                        order.getAmount(), order.getCouponUid(), order.getItems()));
        order.getItems().forEach(item -> {
            kafkaTemplate.send("catalog-events", item.getProductUid().toString(), MetricsEvent.of(item.getProductUid(), MetricsEvent.EventType.PRODUCT_SOLD_EVENT, item.getQuantity()));
        });

    }
}
