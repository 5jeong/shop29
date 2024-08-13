package com.toy2.shop29.order.domain.request;

import com.toy2.shop29.order.domain.ShippingAddressInfoDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderCompletedRequestDTO {
    private List<OrderProductDto> orderItems;
    private Long totalPrice;
    private ShippingAddressInfoDTO shippingAddress;
}
