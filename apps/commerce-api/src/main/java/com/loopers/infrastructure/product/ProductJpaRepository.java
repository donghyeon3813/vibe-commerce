package com.loopers.infrastructure.product;

import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductData;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductJpaRepository extends JpaRepository<Product, Long> {
    @Query(value = """ 
        SELECT new com.loopers.domain.product.ProductData(p.name, p.amount, p.quantity, b.name, COUNT(l.id) AS likeCount)
            FROM Product p
            JOIN Brand b ON p.brandUid = b.id
            LEFT JOIN Like l ON l.productUid = p.id
            WHERE (:brandUid IS NULL OR b.id = :brandUid)
            GROUP BY p.id, p.name, p.amount, p.quantity, b.name""")
    List<ProductData> findAllByPageable(@Param("brandUid") Long brandUid, Pageable pageable);

    List<Product> findIdByIdIn(List<Long> productUids);
}
