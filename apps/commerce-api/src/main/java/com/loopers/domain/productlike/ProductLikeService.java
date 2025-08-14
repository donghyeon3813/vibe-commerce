package com.loopers.domain.productlike;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductLikeService {
    private final ProductLikeRepository productLikeRepository;

    public Optional<ProductLike> getProductLike(Long productUid) {
        return productLikeRepository.findById(productUid);

    }
    @Transactional
    public ProductLike saveProductLike(Long productUid, long likeCount, Long brandUid) {
        return productLikeRepository.save(ProductLike.of(productUid, likeCount, brandUid));
    }
}
