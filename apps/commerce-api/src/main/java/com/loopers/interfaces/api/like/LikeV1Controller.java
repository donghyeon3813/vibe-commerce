package com.loopers.interfaces.api.like;

import com.loopers.application.like.LikeCommand;
import com.loopers.application.like.LikeFacade;
import com.loopers.interfaces.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.loopers.support.error.HeaderConstants.X_USER_ID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/likes")
public class LikeV1Controller implements LikeV1ApiSpec {
    private final LikeFacade likeFacade;

    @PostMapping
    @Override
    public ApiResponse<LikeV1Dto.LikeResponse> like(@RequestHeader(value = X_USER_ID) String userId, @RequestBody LikeV1Dto.LikeRequest request) {
        likeFacade.register(LikeCommand.RegisterDto.of(userId, request.getProductId()));
        return ApiResponse.success(null);
    }

    @DeleteMapping
    @Override
    public ApiResponse<LikeV1Dto.LikeResponse> unlike(@RequestHeader(value = X_USER_ID) String userId, @RequestBody LikeV1Dto.UnlikeRequest request) {
        likeFacade.delete(LikeCommand.DeleteDto.of(userId, request.getProductId()));
        return ApiResponse.success(null);
    }
}
