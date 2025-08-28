package com.loopers.infrastructure.productlike;

import com.loopers.domain.productlike.ProductLike;
import com.loopers.domain.productlike.ProductLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
@Component
@RequiredArgsConstructor
public class ProductLikeRepositoryImpl implements ProductLikeRepository {

    private final ProductLikeJpaRepository productLikeJpaRepository;

    @Override
    public Optional<ProductLike> findById(Long productUid) {
        return productLikeJpaRepository.findById(productUid);
    }

    @Override
    public ProductLike save(ProductLike productLike) {
        return productLikeJpaRepository.save(productLike);
    }

    @Override
    public Optional<ProductLike> findByIdForUpdate(Long productUid) {
        return productLikeJpaRepository.findByIdForUpdate(productUid);
    }

}
