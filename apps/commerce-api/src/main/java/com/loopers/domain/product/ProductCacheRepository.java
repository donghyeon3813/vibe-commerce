package com.loopers.domain.product;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductCacheRepository {
    List<ProductData> findByPageable(Long brandUid, Pageable pageable);

    void setPageable(Long brandUid, Pageable pageable, List<ProductData> productData);
}
