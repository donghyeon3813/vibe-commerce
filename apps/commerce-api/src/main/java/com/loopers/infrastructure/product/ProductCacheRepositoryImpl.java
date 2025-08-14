package com.loopers.infrastructure.product;

import com.loopers.domain.product.ProductCacheRepository;
import com.loopers.domain.product.ProductData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductCacheRepositoryImpl implements ProductCacheRepository {

    private final ProductRedisRepository productRedisRepository;
    private final CacheKey cacheKey;

    @Override
    public List<ProductData> findByPageable(Long brandUid, Pageable pageable) {
        return productRedisRepository.get(cacheKey.buildCacheKey(brandUid, pageable));
    }

    @Override
    public void setPageable(Long brandUid, Pageable pageable, List<ProductData> productData) {

        productRedisRepository.set(cacheKey.buildCacheKey(brandUid, pageable), productData);
    }
}
