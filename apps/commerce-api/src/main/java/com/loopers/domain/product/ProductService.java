package com.loopers.domain.product;

import com.loopers.application.order.OrderCommand;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public Optional<Product> getProductInfo(Long id) {
        return productRepository.findByProductId(id);
    }

    public List<ProductData> getProductList(Long brandUid, Pageable pageable) {
        return productRepository.findByPageable(brandUid, pageable);
    }


    public List<Product> getProductsByProducUids(Set<Long> productUids) {
        return productRepository.findByProductUids(productUids);
    }

    public void checkProductConsistency(int orderProductSize, int productSize) {
        if (orderProductSize != productSize) {
            throw new CoreException(ErrorType.NOT_FOUND, "주문한 상품을 찾을 수 없습니다.");
        }
    }

    public void deductQuantity(List<OrderCommand.Order.OrderItem> items, List<Product> productList) {
        Map<Long, Product> productMap = productList.stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        for (OrderCommand.Order.OrderItem item : items) {
            Product product = productMap.get(item.getProductId());
            product.deductQuantity(item.getQuantity());
        }
    }
}
