package com.loopers.domain.order;

import com.loopers.domain.BaseEntity;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class OrderModel extends BaseEntity {

    private Long userUid;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private BigDecimal amount;

    private Long couponUid;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    public OrderModel(Long userUid, OrderStatus orderStatus, BigDecimal amount, Address address, Long couponId) {
        this.userUid = userUid;
        this.orderStatus = orderStatus;
        this.amount = amount;
        this.address = address;
    }

    public static OrderModel create(Long userUid, BigDecimal amount, Address address, Long couponId) {
        if (userUid == null) {
            throw new CoreException(ErrorType.BAD_REQUEST, "userUid는 없을 수 없습니다.");
        }
        if (amount.doubleValue() <= 0) {
            throw new CoreException(ErrorType.BAD_REQUEST, "amount는 0이하일 수 없습니다.");
        }
        if (address == null) {
            throw new CoreException(ErrorType.BAD_REQUEST, "addres는 null일 수 없습니다.");
        }
        return new OrderModel(userUid, OrderStatus.CREATED, amount, address, couponId);
    }

    public void addOrderItem(OrderItem orderItem) {
        if (orderItem == null) {
            throw new CoreException(ErrorType.BAD_REQUEST, "주문 항목은 비워져 있을 수 없습니다.");
        }
        this.items.add(orderItem);
        orderItem.updateOrderUid(this);
    }

    public void changeStatusToPaid() {
        if (this.orderStatus == OrderStatus.PAID) {
            throw new CoreException(ErrorType.BAD_REQUEST, "이미 결제된 상태는 변경할 수 없습니다.");
        }
        this.orderStatus = OrderStatus.PAID;
    }
}
