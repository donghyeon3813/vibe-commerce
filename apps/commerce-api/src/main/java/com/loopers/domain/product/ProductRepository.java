package com.loopers.domain.product;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Optional<Product> findByProductId(Long id);

    List<ProductData> findByPageable(Long brandUid, Pageable pageable);

    List<Product> findByProductUids(List<Long> productUids);
}
