package com.toy2.shop29.order.domain.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class AddCurrentOrderRequestDto {
    private List<OrderProductDto> orderItems;
}
