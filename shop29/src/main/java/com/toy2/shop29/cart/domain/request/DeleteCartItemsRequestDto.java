package com.toy2.shop29.cart.domain.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DeleteCartItemsRequestDto {
    private Long productId;
    private Long productOptionId;
}
