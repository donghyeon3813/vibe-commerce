package com.loopers.interfaces.api.ranking;

import com.loopers.application.ranking.RankingCommand;
import com.loopers.application.ranking.RankingFacade;
import com.loopers.application.ranking.RankingInfo;
import com.loopers.infrastructure.ranking.RankingDto;
import com.loopers.interfaces.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/v1/rankings")
@RequiredArgsConstructor
public class RankingV1Controller implements RankingV1ApiSpec{
    private final RankingFacade rankingFacade;
    @Override
    @GetMapping
    public ApiResponse<RankingV1Dto.RankingListResponse> getProducts(String date, int size, int page) {

        RankingInfo.RankingList ranKingList = rankingFacade.getRanKingList(RankingCommand.Get.of(date, size, page));
        RankingV1Dto.RankingListResponse response = RankingV1Dto.RankingListResponse.from(ranKingList);
        return ApiResponse.success(response);
    }
}
