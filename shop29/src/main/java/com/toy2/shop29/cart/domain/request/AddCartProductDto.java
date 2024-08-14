package com.toy2.shop29.cart.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddCartProductDto {
    private Long productId;
    private Long quantity;
}
