package com.toy2.shop29.order.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderAddressDTO {
    private String shippingAddressId;
    private String recipientName;
    private String address;
    private String addressDetail;
    private String contact;
    private String subContact;
    private String deliveryNote;
}
