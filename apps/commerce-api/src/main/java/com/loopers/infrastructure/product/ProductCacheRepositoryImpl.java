package com.loopers.infrastructure.product;

import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductCacheRepository;
import com.loopers.domain.product.ProductData;
import com.loopers.support.error.CoreException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductCacheRepositoryImpl implements ProductCacheRepository {

    private final ProductRedisRepository productRedisRepository;
    private final CacheKey cacheKey;

    @Override
    public List<ProductData> findByPageable(Long brandUid, Pageable pageable) {
        try {
            return productRedisRepository.get(cacheKey.buildProductListCacheKey(brandUid, pageable));
        } catch (CoreException e){
            log.error("Redis Exception Fail Get ProductList", e);
            return Collections.emptyList();
        }

    }

    @Override
    public void setPageable(Long brandUid, Pageable pageable, List<ProductData> productData) {
        try {
            productRedisRepository.set(cacheKey.buildProductListCacheKey(brandUid, pageable), productData);
        } catch (CoreException e){
            log.error("Redis Exception Fail Set ProductList", e);
        }
    }

    @Override
    public Optional<Product> findByProductInfo(Long productId) {
        try {
            return productRedisRepository.getProduct(cacheKey.buildProductCacheKey(productId));
        } catch (CoreException e){
            log.error("Redis Exception get ProductInfo", e);
            return Optional.empty();
        }
    }

    @Override
    public void setProduct(Long id, Optional<Product> product) {
        if (product.isEmpty()){
            return;
        }
        try {
            productRedisRepository.setProduct(cacheKey.buildProductCacheKey(id), product.get());
        } catch (CoreException e){
            log.error("Redis Exception Fail Set ProductList", e);
        }
    }
}
