package com.loopers.infrastructure.product;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class CacheKey {
    public String buildProductListCacheKey(Long brandUid, Pageable pageable) {
        long brandId = brandUid == null ? 0 : brandUid;

        Sort.Order order = pageable.getSort().stream()
                .findFirst()
                .orElse(null);
        String sortKey = order == null
                ? "unsorted"
                : order.getProperty() + ":" + order.getDirection();

        return String.format(
                "search:product:brand:%d:page:%d:size:%d:sort:%s",
                brandId,
                pageable.getPageNumber(),
                pageable.getPageSize(),
                sortKey
        );
    }

    public String buildProductCacheKey(Long productId) {
        return String.format(
                "search:product:%d",
                productId
        );
    }
}
