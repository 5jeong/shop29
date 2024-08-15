package com.toy2.shop29.order.domain.response;

import com.toy2.shop29.order.domain.OrderAddressDTO;
import com.toy2.shop29.order.domain.OrderItemDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class OrderHistoryDTO {
    private String orderId;
    private String orderTime;
    private Long totalPrice;
    private String orderStatus;
    private String deliveryStatus;
    private String payMethod;
    private List<OrderItemDTO> orderItems;
    private OrderAddressDTO shippingAddress;
}
