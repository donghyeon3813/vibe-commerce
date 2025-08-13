package com.loopers.infrastructure.product;

import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductData;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface ProductJpaRepository extends JpaRepository<Product, Long> {
    @Query(value = """ 
        SELECT new com.loopers.domain.product.ProductData(p.name, p.amount, p.quantity, b.name,  COALESCE(l.likeCount, 0) as likeCount)
            FROM Product p
            JOIN Brand b ON p.brandUid = b.id
            LEFT JOIN ProductLike l ON l.productId = p.id
            WHERE (:brandUid IS NULL OR b.id = :brandUid)""")
    List<ProductData> findAllByPageable(@Param("brandUid") Long brandUid, Pageable pageable);

    List<Product> findIdByIdIn(Set<Long> productUids);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Product p WHERE p.id IN :ids")
    List<Product> findIdByIdInForUpdate(@Param("ids") Set<Long> ids);
}
