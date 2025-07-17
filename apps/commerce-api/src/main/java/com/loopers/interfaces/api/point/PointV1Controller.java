package com.loopers.interfaces.api.point;

import com.loopers.application.point.PointFacade;
import com.loopers.interfaces.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/points")
@RequiredArgsConstructor
public class PointV1Controller implements PointV1ApiSpec {

    private final PointFacade pointFacade;

    @GetMapping
    @Override
    public ApiResponse<PointV1Dto.PointInfoResponse> getPointInfo(@RequestHeader(value = "X-USER-ID") String userId) {
        PointV1Dto.PointInfoRequest pointInfoRequest = PointV1Dto.PointInfoRequest.of(userId);
        PointV1Dto.PointInfoResponse response = PointV1Dto.PointInfoResponse.from(pointFacade.getPointInfo(pointInfoRequest));
        return ApiResponse.success(response);
    }

    @PostMapping("/charge")
    @Override
    public ApiResponse<PointV1Dto.PointChargeResponse> chargePoint(@RequestBody ChargePointRequest request, @RequestHeader(value = "X-USER-ID") String userId) {
        PointV1Dto.PointChargeRequest pointChargeRequest = PointV1Dto.PointChargeRequest.of(request.getChargePoint(), userId);
        PointV1Dto.PointChargeResponse response = PointV1Dto.PointChargeResponse.from(pointFacade.chargePoint(pointChargeRequest));
        return ApiResponse.success(response);
    }
}
