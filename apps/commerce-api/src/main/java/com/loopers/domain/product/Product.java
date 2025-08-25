package com.loopers.domain.product;

import com.loopers.domain.BaseEntity;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
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

    public void deductQuantity(int quantity) {
        if (quantity <= 0) {
            throw new CoreException(ErrorType.BAD_REQUEST, "수량이 0 이하일 수 없습니다.");
        }
        if (this.quantity < quantity) {
            throw new CoreException(ErrorType.BAD_REQUEST, "재고가 부족합니다.");
        }
        this.quantity -= quantity;
    }

    public void restoreStock(int quantity) {
        this.quantity += quantity;
    }
}
