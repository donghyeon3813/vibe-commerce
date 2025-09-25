package com.loopers.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@Table(name = "product_metrics")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductMetrics extends BaseEntity {
    private Long productId;
    private int likeCount;
    private int saleCount;
    private int viewCount;
    @Column(name = "metrics_date", columnDefinition = "DATE")
    private LocalDate metricsDate;

    public static ProductMetrics create(Long productId) {
        return new ProductMetrics(productId);
    }

    public ProductMetrics(Long productId) {
        this.productId = productId;
        this.metricsDate = LocalDate.now();
    }

    public void adjustLikeCount(int count) {
        this.likeCount+= count;
    }

    public void incrementSaleCount(int count) {
        this.saleCount += count;
    }

    public void incrementViewCount(int count) {
        this.viewCount += count;
    }
}
