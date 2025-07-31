package com.loopers.application.product;

import lombok.Getter;
import org.springframework.data.domain.Sort.Direction;


public class ProductCommand {
    @Getter
    public static class ListInfoRequest {
        Long brandId;
        Sort sort;
        int page;
        int size;

        public ListInfoRequest(Long brandId, Sort sort, int page, int size) {
            this.brandId = brandId;
            this.sort = sort;
            this.page = page;
            this.size = size;
        }

        @Getter
        public enum Sort {
            LATEST("saleStartDateTime", Direction.DESC),
            PRICE_ASC("amount", Direction.ASC),
            LIKE_DESC("likeCount", Direction.DESC);

            String field;
            Direction direction;


            Sort(String field, org.springframework.data.domain.Sort.Direction desc) {
                this.field = field;
                this.direction = desc;
            }

        }

        public static ListInfoRequest of(Long brandId, Sort sort, int page, int size) {
            return new ListInfoRequest(brandId, sort, page, size);
        }
    }

    public record DetailInfoRequest(Long productId) {
        public static DetailInfoRequest of(Long productId) {
            return new DetailInfoRequest(productId);
        }

    }
}
