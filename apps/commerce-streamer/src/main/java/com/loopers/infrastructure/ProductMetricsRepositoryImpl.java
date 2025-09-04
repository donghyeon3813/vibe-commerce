package com.loopers.infrastructure;

import com.loopers.domain.metrics.ProductMetrics;
import com.loopers.domain.metrics.ProductMetricsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductMetricsRepositoryImpl implements ProductMetricsRepository {
    private final ProductMetricsJpaRepository repository;

    @Override
    public ProductMetrics save(ProductMetrics productMetrics) {
        return repository.save(productMetrics);
    }

    @Override
    public Optional<ProductMetrics> findByProductIdAndMetricsDate(Long productId, LocalDate today) {
        return repository.findByProductIdAndMetricsDate(productId, today);
    }
}
