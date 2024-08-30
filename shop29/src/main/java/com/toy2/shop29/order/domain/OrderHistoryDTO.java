package com.toy2.shop29.order.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderHistoryDTO {
    private String orderId;
    private String userId;
    private String orderTime;
    private Long totalPrice;
    private String orderStatus;
    private String deliveryStatus;
    private String payMethod;
    private String tid;
    private String updatedTime;
}
