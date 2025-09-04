package com.loopers.infrastructure.cache;

import java.util.List;

public class ProductCacheKey {
    public static final List<String> PRODUCT_LIST_KET = List.of(
            "search:product:brand:0:page:1:size:20:sort:likeCount:DESC",
            "search:product:brand:0:page:2:size:20:sort:likeCount:DESC",
            "search:product:brand:0:page:3:size:20:sort:likeCount:DESC"
    );

    public static final List<String> PRODUCT_DETAIL_KEY = List.of(
            "search:product:1"
    );
}
