package com.loopers.infrastructure.product;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class CacheKey {
    public String buildCacheKey(Long brandUid, Pageable pageable) {
        int brandId = brandUid == null ? 0 : brandUid.intValue();

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
}
