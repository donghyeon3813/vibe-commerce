package com.loopers.domain.metrics;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductMetricsService {

    private final ProductMetricsRepository productMetricsRepository;

    public ProductMetrics save(Long productId) {
        ProductMetrics productMetrics = ProductMetrics.create(productId);
        return productMetricsRepository.save(productMetrics);
    }

    public Optional<ProductMetrics> findByProductIdAndMetricsDate(Long productId, LocalDate today) {
        return productMetricsRepository.findByProductIdAndMetricsDate(productId, today);
    }
}
