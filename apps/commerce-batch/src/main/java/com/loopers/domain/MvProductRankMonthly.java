package com.loopers.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@Table(name = "mv_product_rank_monthly")
@NoArgsConstructor
public class MvProductRankMonthly extends BaseEntity {
    private Long productId;
    private LocalDate aggDate;
    private double score;

    private MvProductRankMonthly(Long productId, LocalDate aggDate, double score) {
        this.productId = productId;
        this.aggDate = aggDate;
        this.score = score;
    }
    public static MvProductRankMonthly create(Long productId, LocalDate aggDate, double score) {
        return new MvProductRankMonthly(productId, aggDate, score);
    }
}
