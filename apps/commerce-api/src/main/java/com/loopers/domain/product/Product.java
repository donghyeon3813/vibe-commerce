package com.loopers.domain.product;

import com.loopers.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "product")
public class Product extends BaseEntity {
    private Long brandUid;
    private String name;
    private int amount;
    private int quantity;

    private Product(Long brandUid, String name, int amount, int quantity) {
        this.brandUid = brandUid;
        this.name = name;
        this.amount = amount;
        this.quantity = quantity;
    }

    public static Product create(Long brandUid, String name, int amount, int quantity) {
        return new Product(brandUid, name, amount, quantity);
    }
}
