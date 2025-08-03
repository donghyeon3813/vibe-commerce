package com.loopers.domain.order;

import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
public class Address {
    private String address;
    private String phone;
    private String receiverName;

    public Address(String address, String phone, String receiverName) {
        this.address = address;
        this.phone = phone;
        this.receiverName = receiverName;
    }

    public static Address of(String address, String phone, String receiverName) {
        return new Address(address, phone, receiverName);
    }
}
