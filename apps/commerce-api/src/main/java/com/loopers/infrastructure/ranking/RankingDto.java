package com.loopers.infrastructure.ranking;

import lombok.Getter;

@Getter
public class RankingDto {
    private Long productId;
    private double score;

    public RankingDto(Long productId, double score) {
        this.productId = productId;
        this.score = score;
    }

}
