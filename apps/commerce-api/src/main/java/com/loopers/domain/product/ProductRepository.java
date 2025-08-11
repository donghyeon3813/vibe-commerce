package com.loopers.domain.product;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ProductRepository {
    Optional<Product> findByProductId(Long id);

    List<ProductData> findByPageable(Long brandUid, Pageable pageable);

    List<Product> findByProductUids(Set<Long> productUids);

    List<Product> findByProductUidsForUpdate(Set<Long> productUids);
}
