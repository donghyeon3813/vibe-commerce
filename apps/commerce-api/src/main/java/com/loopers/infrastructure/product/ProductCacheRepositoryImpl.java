package com.loopers.infrastructure.product;

import com.loopers.domain.product.ProductCacheRepository;
import com.loopers.domain.product.ProductData;
import com.loopers.support.error.CoreException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductCacheRepositoryImpl implements ProductCacheRepository {

    private final ProductRedisRepository productRedisRepository;
    private final CacheKey cacheKey;

    @Override
    public List<ProductData> findByPageable(Long brandUid, Pageable pageable) {
        try {
            return productRedisRepository.get(cacheKey.buildCacheKey(brandUid, pageable));
        } catch (CoreException e){
            log.error("Redis Exception", e);
            return Collections.emptyList();
        }

    }

    @Override
    public void setPageable(Long brandUid, Pageable pageable, List<ProductData> productData) {
        try {
            productRedisRepository.set(cacheKey.buildCacheKey(brandUid, pageable), productData);
        } catch (CoreException e){
            log.error("Redis Exception", e);
        }
    }
}
