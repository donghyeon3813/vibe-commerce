package com.loopers.application.ranking;

import lombok.Getter;

import java.util.List;

public class RankingInfo {
    @Getter
    public static class RankingList{
        private List<RankingDTO> rankings;
        public static RankingList of(List<RankingDTO> list){
            return new RankingList(list);
        }

        public RankingList(List<RankingDTO> rankings) {
            this.rankings = rankings;
        }


    }

}
