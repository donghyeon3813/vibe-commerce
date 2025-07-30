package com.loopers.application.product;

import com.loopers.domain.brand.Brand;
import com.loopers.domain.brand.BrandService;
import com.loopers.domain.like.LikeService;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductDetailComposer;
import com.loopers.domain.product.ProductService;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductFacade {
    private final ProductService productService;
    private final BrandService brandService;
    private final LikeService likeService;
    private final ProductDetailComposer productDetailComposer;


//    public void getProductList(ProductCommand.ListInfoRequest productCommand) {
//     productService.getProductList();
//    }

    public ProductInfo.ProductDetailInfo getProductDetailInfo(ProductCommand.DetailInfoRequest getInfo) {
        Optional<Product> productOptional = productService.getProductInfo(getInfo.productId());

        if (productOptional.isEmpty()) {
            throw new CoreException(ErrorType.NOT_FOUND, "상품 정보가 존재하지 않습니다.");
        }

        Product product = productOptional.get();

        Optional<Brand> Brand = brandService.getBrandInfo(product.getBrandUid());
        if (Brand.isEmpty()) {
            throw new CoreException(ErrorType.NOT_FOUND, "Brand 정보가 존재하지 않습니다.");
        }
        Brand brand = Brand.get();
        int count = likeService.getCountByProductUid(product.getId());

        return ProductInfo.ProductDetailInfo.from(productDetailComposer.compose(product, brand, count));
    }
}
