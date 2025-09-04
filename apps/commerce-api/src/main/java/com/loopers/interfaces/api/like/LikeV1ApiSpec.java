package com.loopers.interfaces.api.like;

import com.loopers.interfaces.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

public interface LikeV1ApiSpec {
    @Tag(name = "Like V1 API", description = "상품 좋아요 API입니다.")
    @Operation(
            summary = "상품 좋아요 요청",
            description = "입력된 상품의 좋아요 처리를 합니다."
    )
    ApiResponse<LikeV1Dto.LikeResponse> like(String userId, LikeV1Dto.LikeRequest request);
    @Tag(name = "Like V1 API", description = "상품 좋아요 해제 API입니다.")
    @Operation(
            summary = "상품 좋아요 요청",
            description = "입력된 상품의 좋아요 처리를 합니다."
    )
    ApiResponse<LikeV1Dto.LikeResponse> unlike(String userId, LikeV1Dto.UnlikeRequest request);

}
