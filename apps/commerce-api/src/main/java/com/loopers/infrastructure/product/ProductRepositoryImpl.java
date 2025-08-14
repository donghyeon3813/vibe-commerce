package com.loopers.infrastructure.product;

import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductData;
import com.loopers.domain.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {
    private final ProductJpaRepository productJpaRepository;
    private final ProductQueryDslImpl productQueryDsl;

    @Override
    public Optional<Product> findByProductId(Long id) {
        return productJpaRepository.findById(id);
    }

    @Override
    public List<ProductData> findByPageable(Long brandUid, Pageable pageable) {
        return productQueryDsl.findAllByPageable(brandUid, pageable);
    }

    @Override
    public List<Product> findByProductUids(Set<Long> productUids) {
        return productJpaRepository.findIdByIdIn(productUids);
    }

    @Override
    public List<Product> findByProductUidsForUpdate(Set<Long> productUids) {
        return productJpaRepository.findIdByIdInForUpdate(productUids);
    }


}
