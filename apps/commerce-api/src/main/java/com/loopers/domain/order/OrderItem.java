package com.loopers.domain.order;

import com.loopers.domain.BaseEntity;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_item")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class OrderItem extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private OrderModel order;
    private Long productUid;
    private int quantity;

    private OrderItem(OrderModel order, Long productUid, int quantity) {
        this.order = order;
        this.productUid = productUid;
        this.quantity = quantity;
    }

    public static OrderItem create(Long productUid, int quantity) {
        if (productUid == null) {
            throw new CoreException(ErrorType.BAD_REQUEST, "productUid는 null 일 수 없습니다.");
        }
        if (quantity <= 0) {
            throw new CoreException(ErrorType.BAD_REQUEST, "quantity는 0 이하 일 수 없습니다.");
        }
        return new OrderItem(null, productUid, quantity);
    }

    public void updateOrderUid(OrderModel orderModel) {
        this.order = orderModel;
    }
}
