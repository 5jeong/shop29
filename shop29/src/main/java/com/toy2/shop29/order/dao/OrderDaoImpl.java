package com.toy2.shop29.order.dao;

import com.toy2.shop29.order.domain.OrderDto;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderDaoImpl implements OrderDao {
    @Autowired
    private SqlSession session;

    private static String namespace = "com.toy2.shop29.order.dao.OrderDaoMapper.";

    // 해당 유저 주문내역 조회
    @Override
    public OrderDto findOrder(Long id) throws Exception {
        return session.selectOne(namespace + "findOrder", id);
    }

    // 주문내역 전체 조회
    @Override
    public List<OrderDto> findAllOrders(String id) throws Exception {
        return session.selectList(namespace + "findAllOrders", id);
    }

    @Override
    public int saveOrder(OrderDto order) throws Exception {
        return 0;
    }

    @Override
    public int updateOrder(OrderDto order) throws Exception {
        return 0;
    }

    @Override
    public int deleteOrder(Long id) throws Exception {
        return 0;
    }
}
