package com.loopers.domain.metrics;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductMetricsService {

    private final ProductMetricsRepository productMetricsRepository;

    public Optional<ProductMetrics> findByProductId(Long productId) {
        return productMetricsRepository.findByProductId(productId);
    }

    public ProductMetrics save(Long productId) {
        ProductMetrics productMetrics = ProductMetrics.create(productId);
        return productMetricsRepository.save(productMetrics);
    }
}
