package com.loopers.domain.like;

import com.loopers.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "likes")
@Getter
public class Like extends BaseEntity {
    @Column(unique = true)
    private Long userUid;
    @Column(unique = true)
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
