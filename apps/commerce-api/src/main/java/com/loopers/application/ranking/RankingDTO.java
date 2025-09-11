package com.loopers.application.ranking;

import lombok.Getter;

@Getter
public class RankingDTO {
    private String productName;
    private int amount;
    private int quantity;
    private String brandName;
    private int rank;

    public RankingDTO(String productName, int amount, int quantity, String brandName, int rank) {
        this.productName = productName;
        this.amount = amount;
        this.quantity = quantity;
        this.brandName = brandName;
        this.rank = rank;
    }
}
