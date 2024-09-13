package com.toy2.shop29.cart.domain.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DeleteCartItemsRequestDto {
    private Long productId;
    private Long productOptionId;
}
