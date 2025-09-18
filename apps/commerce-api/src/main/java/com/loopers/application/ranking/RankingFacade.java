package com.loopers.application.ranking;

import com.loopers.application.like.LikeInfo;
import com.loopers.domain.brand.Brand;
import com.loopers.domain.brand.BrandService;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductData;
import com.loopers.domain.product.ProductDetailComposer;
import com.loopers.domain.product.ProductService;
import com.loopers.domain.ranking.RankingService;
import com.loopers.infrastructure.ranking.RankingDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RankingFacade {
    private final RankingService rankingService;
    private final ProductService productService;
    private final BrandService brandService;
    private final ProductDetailComposer productDetailComposer;

    public RankingInfo.RankingList getRanKingList(RankingCommand.Get command) {
        // 1. Redis에서 랭킹 데이터 가져오기
        List<RankingDto> rankingList = new ArrayList<>();
        switch (command.getPeriod()){
            case DAILY -> rankingList = rankingService.getRanKingList(command);
            case WEEKLY -> rankingList = rankingService.getWeeklyRanKingList(command);
            case MONTHLY -> rankingList = rankingService.getMontlhyRanKingList(command);
        }
        System.out.println("rankingList.size() = " + rankingList.size());
        if (rankingList.isEmpty()) {
            return new RankingInfo.RankingList(Collections.emptyList());
        }

        // 2. productIds 추출
        Set<Long> productIds = rankingList.stream()
                .map(RankingDto::getProductId)
                .collect(Collectors.toSet());
        System.out.println("productIds = " + productIds);

        // 3. 상품 조회
        List<Product> products = productService.getProductsByProducUids(productIds);
        System.out.println("products.size() = " + products.size());

        // 4. 브랜드 조회
        List<Long> brandUids = products.stream()
                .map(Product::getBrandUid)
                .toList();

        List<Brand> brands = brandService.getBrandsByBrandUids(brandUids);
        System.out.println("brands.size() = " + brands.size());

        // 5. 상품 + 브랜드 정보 조합
        List<ProductData> productDatas = productDetailComposer.composeList(products, brands, Collections.emptyList());
        System.out.println("productDatas.size() = " + productDatas.size());
        // 6. productId → ProductData 매핑
        Map<Long, ProductData> productDataMap = productDatas.stream()
                .collect(Collectors.toMap(
                        p -> products.stream()
                                .filter(prod -> prod.getName().equals(p.productName()))
                                .findFirst()
                                .orElseThrow()
                                .getId(),
                        p -> p
                ));

        // 7. 랭킹순으로 RankingDTO 변환
        List<RankingDTO> rankingDTOs = new ArrayList<>();
        int rank = 1;
        for (RankingDto ranking : rankingList) {
            ProductData pd = productDataMap.get(ranking.getProductId());
            if (pd != null) {
                RankingDTO dto = new RankingDTO(
                        pd.productName(),
                        pd.amount(),
                        pd.quantity(),
                        pd.brandName(),
                        rank++
                );
                rankingDTOs.add(dto);
            }
        }
        System.out.println("rankingDTOs = " + rankingDTOs);

        return new RankingInfo.RankingList(rankingDTOs);
    }
}
