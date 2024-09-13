package com.toy2.shop29.order.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderItemDTO {
    private Long productId;
    private String productName;
    private Long quantity;
    private Long price;
    private int saleRatio;
    private Long productOptionId;
    private String optionValueName;
    private String productOrderStatus;
}
