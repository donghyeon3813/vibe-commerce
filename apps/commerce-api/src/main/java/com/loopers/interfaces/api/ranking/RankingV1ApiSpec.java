package com.loopers.interfaces.api.ranking;

import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.product.ProductV1Dto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Ranking V1 API", description = "랭킹 API 입니다.")
public interface RankingV1ApiSpec {
    @Operation(
            summary = "랭킹 페이지 조회",
            description = "date, size, page를 통 랭킹 page를 조회해옵니다."
    )
    ApiResponse<RankingV1Dto.RankingListResponse> getProducts(String date, int size, int page, String period);
}
