package com.loopers.interfaces.api.point;

import com.loopers.application.point.PointFacade;
import com.loopers.interfaces.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
