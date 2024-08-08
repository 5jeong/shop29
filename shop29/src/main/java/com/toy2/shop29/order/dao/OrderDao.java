package com.toy2.shop29.order.dao;

import com.toy2.shop29.order.domain.OrderDto;

import java.util.List;

public interface OrderDao {
    // 해당 유저 주문내역 조회
    OrderDto findOrder(Long id) throws Exception;
    // 주문내역 전체 조회
    List<OrderDto> findAllOrders(String id) throws Exception;
    // 주문 성공 시 주문 내역 저장
    int saveOrder(OrderDto order) throws Exception;
    // 주문 내역 수정
    int updateOrder(OrderDto order) throws Exception;
    // 주문 내역 삭제
    int deleteOrder(Long id) throws Exception;
}
