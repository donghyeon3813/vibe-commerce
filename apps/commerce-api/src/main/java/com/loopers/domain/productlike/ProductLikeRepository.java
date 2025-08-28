package com.loopers.domain.productlike;

import java.util.Optional;

public interface ProductLikeRepository {
    Optional<ProductLike> findById(Long productUid);

    ProductLike save(ProductLike productLike);

    Optional<ProductLike> findByIdForUpdate(Long productUid);
}
