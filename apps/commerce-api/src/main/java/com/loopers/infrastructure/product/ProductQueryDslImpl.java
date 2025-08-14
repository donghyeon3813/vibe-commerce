package com.loopers.infrastructure.product;

import com.loopers.domain.brand.QBrand;
import com.loopers.domain.product.ProductData;
import com.loopers.domain.product.QProduct;
import com.loopers.domain.productlike.QProductLike;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;


import java.util.List;

@RequiredArgsConstructor
@Component
public class ProductQueryDslImpl {
    private final JPAQueryFactory jpaQueryFactory;

    public List<ProductData> findAllByPageable(Long brandUid, Pageable pageable) {
        QProduct p = QProduct.product;
        QBrand b = QBrand.brand;
        QProductLike l = QProductLike.productLike;

        JPAQuery<ProductData> query = jpaQueryFactory
                .select(Projections.constructor(
                        ProductData.class,
                        p.name,
                        p.amount,
                        p.quantity,
                        b.name,
                        l.likeCount
                ))
                .from(p)
                .join(l).on(l.productId.eq(p.id))
                .join(b).on(p.brandUid.eq(b.id));
        BooleanExpression brandFilter = null;
        for (Sort.Order order : pageable.getSort()) {
            Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;

            switch (order.getProperty()) {
                case "saleStartDateTime":
                    brandFilter = p.brandUid.eq(brandUid);
                    query.orderBy(new OrderSpecifier<>(direction, p.saleStartDateTime));
                    break;
                case "amount":
                    brandFilter = p.brandUid.eq(brandUid);
                    query.orderBy(new OrderSpecifier<>(direction, p.amount));
                    break;
                case "likeCount":
                    brandFilter = l.brandUid.eq(brandUid);
                    query.orderBy(new OrderSpecifier<>(direction, l.likeCount));
                    break;
            }
        }
        if (brandFilter != null) {
            query.where(brandFilter);
        }

        return query.offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }
}
