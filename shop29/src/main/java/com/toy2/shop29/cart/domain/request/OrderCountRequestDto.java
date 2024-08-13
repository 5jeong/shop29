package com.toy2.shop29.cart.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderCountRequestDto {
    private Long product_id;
    private Long quantity;
}
