package com.toy2.shop29.order.service;

import com.toy2.shop29.order.domain.OrderDto;

import java.util.List;

public interface OrderService {
    OrderDto getOrder() throws Exception;
    List<OrderDto> getAllOrders() throws Exception;
}
