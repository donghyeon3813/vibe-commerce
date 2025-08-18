package com.loopers.domain.productlike;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "product_like")
@Getter
public class ProductLike {
    @Id
    private Long productId;
    private long likeCount;
    private Long brandUid;
    @Version
    private long version;

    private ProductLike(Long productId, long likeCount, long brandUid) {
        this.productId = productId;
        this.likeCount = likeCount;
        this.brandUid = brandUid;
    }

    public static ProductLike of(Long productId, long likeCount, Long brandUid) {
        if(productId == null){
            throw new CoreException(ErrorType.BAD_REQUEST, "productId는 null 일 수 없습니다.");
        }
        if(brandUid == null){
            throw new CoreException(ErrorType.BAD_REQUEST, "productId는 null 일 수 없습니다.");
        }
        return new ProductLike(productId, likeCount, brandUid);
    }

    public void increment() {
        this.likeCount++;
    }

    public void decrement() {
        if(this.likeCount == 0){
            throw new CoreException(ErrorType.BAD_REQUEST, "좋아요 수는 0이하가 될 수 없습니다.");
        }
        this.likeCount--;
    }
}
