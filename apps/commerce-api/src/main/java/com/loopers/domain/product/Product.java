package com.loopers.domain.product;

import com.loopers.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "product")
public class Product extends BaseEntity {
    private Long brandUid;
    private String name;
    private int amount;
    private int quantity;
    private LocalDateTime saleStartDateTime;

    private Product(Long brandUid, String name, int amount, int quantity) {
        this.brandUid = brandUid;
        this.name = name;
        this.amount = amount;
        this.quantity = quantity;
        this.saleStartDateTime = LocalDateTime.now();
    }

    public static Product create(Long brandUid, String name, int amount, int quantity) {
        return new Product(brandUid, name, amount, quantity);
    }
}
