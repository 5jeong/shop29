package com.toy2.shop29.order.domain.request;

import java.util.List;

public class AddCurrentOrderRequest {
    public List<OrderProductDto> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderProductDto> orderItems) {
        this.orderItems = orderItems;
    }

    private List<OrderProductDto> orderItems;
}
