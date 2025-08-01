package com.loopers.domain.brand;

import com.loopers.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "brand")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Brand extends BaseEntity {
    private String name;

    private Brand(String name) {
        this.name = name;
    }

    public static Brand create(String name) {
        return new Brand(name);
    }

}
