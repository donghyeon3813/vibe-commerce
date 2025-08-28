package com.loopers.application.productLike.listener;

import com.loopers.domain.productlike.ProductLike;
import com.loopers.domain.productlike.ProductLikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Optional;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

@Component
@RequiredArgsConstructor
@Slf4j

public class ProductLikeListener {
    private final ProductLikeService productLikeService;

    @Transactional(propagation = REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void productLikeIncrement(ProductLikeEvent.LikeIncrementEvent event) {
        log.info("Product like increment event: {}", event);
        Optional<ProductLike> productLikeForUpdate = productLikeService.getProductLikeForUpdate(event.getProductId());
        productLikeForUpdate.ifPresent(ProductLike::increment);
        log.info("product like increment{}", productLikeForUpdate.get().getLikeCount());
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void productLikeDecrement(ProductLikeEvent.LikeDecrementEvent event) {
        log.info("Product like decrement event: {}", event);
        Optional<ProductLike> productLikeForUpdate = productLikeService.getProductLike(event.getProductId());
        productLikeForUpdate.ifPresent(ProductLike::decrement);
    }
}
