package com.loopers.interfaces.api.product;

import com.loopers.application.product.ProductCommand;
import com.loopers.application.product.ProductFacade;
import com.loopers.application.product.ProductInfo;
import com.loopers.interfaces.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/products")
@RequiredArgsConstructor
public class ProductV1Controller implements ProductV1ApiSpec{

    private final ProductFacade productFacade;

    @GetMapping
    @Override
    public ApiResponse<ProductV1Dto.ProductListResponse> getProducts(
            @RequestParam("brandId") Long brandId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "sort", defaultValue = "LIKE_DESC") String sort
    ) {
        ProductInfo.ProductListInfo listInfo = productFacade.getProductList(ProductCommand.ListInfoRequest
                .of( brandId, ProductCommand.ListInfoRequest.Sort.valueOf(sort), page, size));

        return ApiResponse.success(ProductV1Dto.ProductListResponse.from(listInfo));
    }

    @GetMapping("/{productId}")
    @Override
    public ApiResponse<ProductV1Dto.ProductDetailResponse> getProducts(@PathVariable("productId") Long productId) {
        ProductInfo.ProductDetailInfo info = productFacade.getProductDetailInfo(ProductCommand.DetailInfoRequest.of(productId));
        return ApiResponse.success(ProductV1Dto.ProductDetailResponse.from(info));
    }

}
