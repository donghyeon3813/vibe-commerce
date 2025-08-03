package com.loopers.domain.like;

import com.loopers.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "likes", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_uid", "product_uid"})
})
public class Like extends BaseEntity {
    private Long userUid;
    private Long productUid;


    public Like() {

    }

    public Like(Long userUid, Long productUid) {

        this.userUid = userUid;
        this.productUid = productUid;
    }

    public static Like create(Long userUid, Long productUid) {
        return new Like(userUid, productUid);
    }
}
