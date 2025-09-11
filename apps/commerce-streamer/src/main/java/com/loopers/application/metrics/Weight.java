package com.loopers.application.metrics;

import lombok.Getter;

@Getter
public enum Weight {
    LIKE(0.2), VIEW(0.1), SOLD(0.6);
    private double weight;


    Weight(double weight) {
        this.weight = weight;
    }
}
