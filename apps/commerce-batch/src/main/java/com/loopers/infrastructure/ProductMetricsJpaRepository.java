package com.loopers.infrastructure;

import com.loopers.domain.ProductMetrics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProductMetricsJpaRepository extends JpaRepository<ProductMetrics, Long> {
    Optional<ProductMetrics> findByProductIdAndMetricsDate(Long productId, LocalDate today);

    List<ProductMetrics> findAllByMetricsDateBetween(LocalDate startDate, LocalDate endDate);
}
