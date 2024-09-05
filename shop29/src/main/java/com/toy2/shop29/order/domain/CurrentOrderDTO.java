package com.toy2.shop29.order.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class CurrentOrderDTO {
    private Long orderId;
    private String userId;
    private List<OrderItemDTO> orderItems;
}
