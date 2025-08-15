package com.loopers.domain.product;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductCacheRepository {
    List<ProductData> findByPageable(Long brandUid, Pageable pageable);

    void setPageable(Long brandUid, Pageable pageable, List<ProductData> productData);

    Optional<Product> findByProductInfo(Long id);

    void setProduct(Long id, Optional<Product> product);
}
