package com.toy2.shop29.order.domain.response;

import com.toy2.shop29.order.domain.OrderItemDTO;
import com.toy2.shop29.order.domain.ShippingAddressInfoDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class OrderPageResponseDTO {
    private ShippingAddressInfoDTO shippingAddress;
    private List<OrderItemDTO> currentItemsData;
}
