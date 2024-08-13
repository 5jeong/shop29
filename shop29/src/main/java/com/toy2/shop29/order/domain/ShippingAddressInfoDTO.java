package com.toy2.shop29.order.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShippingAddressInfoDTO {
    private Long shippingAddressId;
    private String recipientName;
    private String address;
    private String addressDetail;
    private String contact;
    private String subContact;
    private String deliveryNote;
    private String orderId;
    private int isDefault;
}