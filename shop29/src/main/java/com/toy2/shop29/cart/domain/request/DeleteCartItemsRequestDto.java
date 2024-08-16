package com.toy2.shop29.cart.domain.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class DeleteCartItemsRequestDto {
    private List<Long> productIds;

}
