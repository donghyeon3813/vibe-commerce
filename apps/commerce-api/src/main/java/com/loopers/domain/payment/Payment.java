package com.loopers.domain.payment;

import com.loopers.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payment")
@NoArgsConstructor
public class Payment extends BaseEntity {
    private Long orderUid;

    @Enumerated(EnumType.STRING)
    private PayType payType;

    private String transactionKey;

    public Payment(Long orderUid, PayType payType, String transactionKey) {
        this.orderUid = orderUid;
        this.payType = payType;
        this.transactionKey = transactionKey;
    }

    public static Payment create(Long orderUid, PayType payType, String transactionKey) {
        return new Payment(orderUid, payType, transactionKey);
    }
}
