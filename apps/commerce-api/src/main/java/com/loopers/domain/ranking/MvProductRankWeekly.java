package com.loopers.domain.ranking;

import com.loopers.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@Table(name = "mv_product_rank_weekly")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MvProductRankWeekly extends BaseEntity {
    private Long productId;
    private LocalDate aggDate;
    private double score;

    private MvProductRankWeekly(Long productId, LocalDate aggDate, double score) {
        this.productId = productId;
        this.aggDate = aggDate;
        this.score = score;
    }

    public static MvProductRankWeekly of(Long productId, LocalDate aggDate, double score) {
        return new MvProductRankWeekly(productId, aggDate, score);
    }

}
