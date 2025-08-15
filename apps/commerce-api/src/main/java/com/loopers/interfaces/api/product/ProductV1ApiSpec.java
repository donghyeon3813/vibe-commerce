package com.loopers.interfaces.api.product;

import com.loopers.interfaces.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Product V1 API", description = "상품 API 입니다.")
public interface ProductV1ApiSpec {
    @Operation(
            summary = "상품 목록 조회",
            description = "brand Id, page, size, sort 를 통해 상품 목록을 가져옵니다."
    )
    ApiResponse<ProductV1Dto.ProductListResponse> getProducts(Long brandId, int page, int size, String sort);

    @Operation(
            summary = "상품 정보 조회",
            description = "productId 를 통해 상품 정보를 가져옵니다."
    )
    ApiResponse<ProductV1Dto.ProductDetailResponse> getProducts(Long productId);

}
