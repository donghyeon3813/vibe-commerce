package com.loopers.domain.payment;

import com.loopers.domain.BaseEntity;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payment")
@NoArgsConstructor
@Getter
public class Payment extends BaseEntity {
    private Long orderUid;

    @Enumerated(EnumType.STRING)
    @Column(name = "pay_type", length = 20, nullable = false)
    private PayType payType;
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", length = 20)
    private PaymentStatus paymentStatus;
    private String transactionKey;

    public Payment(Long orderUid, PayType payType, String transactionKey, PaymentStatus pending) {
        this.orderUid = orderUid;
        this.payType = payType;
        this.transactionKey = transactionKey;
        this.paymentStatus = pending;
    }

    public static Payment create(Long orderUid, PayType payType, String transactionKey) {
        return new Payment(orderUid, payType, transactionKey, PaymentStatus.PENDING);
    }

    public void fail(){
        this.paymentStatus = PaymentStatus.FAILED;
    }

    public void requestSuccess(String transactionKey){
        if(transactionKey == null){
            throw new CoreException(ErrorType.BAD_REQUEST, "transactionKey is null");
        }
        this.transactionKey = transactionKey;
    }

    public void success() {
        this.paymentStatus = PaymentStatus.SUCCEED;
    }
}
