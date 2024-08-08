package com.toy2.shop29.order.dao;

import com.toy2.shop29.order.domain.OrderDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OrderDaoTest {

    @Autowired
    private OrderDaoImpl orderDao;

    @Test
    @DisplayName("주문번호로 하나의 주문내역 조회")
    void testFindOrder() throws Exception {
        Long testOrderId = 1L;

        OrderDto result = orderDao.findOrder(testOrderId);

        assertThat(result).isNotNull();
        assertThat(result.getOrder_id()).isEqualTo(testOrderId);
    }

    @Test
    @DisplayName("사용자의 전체 주문내역 조회")
    void testFindAllOrders() throws Exception {
        String testUserId = "user001";

        List<OrderDto> result = orderDao.findAllOrders(testUserId);
        assertThat(result).isNotNull();
    }
}