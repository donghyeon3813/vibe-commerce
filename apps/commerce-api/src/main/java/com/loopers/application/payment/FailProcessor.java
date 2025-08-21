package com.loopers.application.payment;

import com.loopers.domain.order.OrderModel;
import com.loopers.domain.payment.Payment;
import com.loopers.domain.product.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class FailProcessor implements PaymentUpdateProcessor{
    private final ProductService productService;

    @Transactional
    @Override
    public void process(OrderModel order, Payment payment) {
        log.info("Processing fail payment {}", payment);
        order.getItems().forEach(item -> productService.getProductInfo(item.getProductUid())
                .ifPresent(product -> product.restoreStock(item.getQuantity())));
        order.changeStatusToFailed();
        payment.fail();
    }

    @Override
    public boolean check(String status) {
        return "FAILED".equals(status);
    }
}
