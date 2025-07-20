package com.loopers.interfaces.api.point;

import com.loopers.interfaces.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Point V1 API", description = "포인트 API 입니다.")
public interface PointV1ApiSpec {
    @Operation(
            summary = "포인트 조회",
            description = "header 의 X-USER-ID를 통해 포인트 정보를 가져옵니다."
    )
    ApiResponse<PointV1Dto.PointInfoResponse> getPointInfo(
            @Schema(name = "header 의 X-USER-ID 값을 받아옵니다.")
            String userId
    );

    @Operation(
            summary = "포인트 충전",
            description = "입력된 포인트를 충전합니다."
    )
    ApiResponse<PointV1Dto.PointChargeResponse> chargePoint(
            @Schema(name = "userId와 충전될 포인트를 입력합니다.")
            ChargePointRequest request, String userUid
    );
}
