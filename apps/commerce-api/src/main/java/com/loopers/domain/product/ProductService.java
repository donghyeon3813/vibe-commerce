package com.loopers.domain.product;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public Optional<Product> getProductInfo(Long id) {
        return productRepository.findByProductId(id);
    }

    public List<ProductData> getProductList(Long brandUid, Pageable pageable) {
        return productRepository.findByPageable(brandUid, pageable);
    }
}
