package com.loopers.infrastructure.productlike;

import com.loopers.domain.productlike.ProductLike;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductLikeJpaRepository extends JpaRepository<ProductLike, Long> {
    @Query(value = "SELECT pl FROM ProductLike pl WHERE pl.productId = :productUid")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<ProductLike> findByIdForUpdate(@Param("productUid") Long productUid);
}
