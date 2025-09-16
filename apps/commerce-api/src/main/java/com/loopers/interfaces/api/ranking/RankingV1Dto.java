package com.loopers.interfaces.api.ranking;

import com.loopers.application.ranking.RankingInfo;
import lombok.Getter;

import java.util.List;

public class RankingV1Dto {
    @Getter
    public static class RankingListResponse {
        private RankingInfo.RankingList rankingInfo;
        public static RankingListResponse from(RankingInfo.RankingList ranKingList) {
            return new RankingListResponse(ranKingList);
        }

        public RankingListResponse(RankingInfo.RankingList rankingInfo) {
            this.rankingInfo = rankingInfo;
        }
    }
}
