package com.toy2.shop29.cart.domain.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartDto {
    private Long productId;
    private Long quantity;
    private String productName;
    private Long price;
    private Long saleRatio;
    private String sizeInfo;
}
