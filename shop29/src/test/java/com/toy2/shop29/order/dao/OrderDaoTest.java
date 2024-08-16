package com.toy2.shop29.order.dao;

import com.toy2.shop29.order.domain.CurrentOrderDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OrderDaoTest {

    @Autowired
    private OrderDao orderDao;

    @BeforeEach
    void setUp() throws Exception {
        orderDao.deleteAllCurrentOrder();
        orderDao.deleteAllCurrentOrderItem();
    }

    @Test
    @DisplayName("주문 처리 내역 추가 테스트")
    void testInsertCurrentOrder() throws Exception {
        assertEquals(0, orderDao.countCurrentOrder());
        orderDao.insertCurrentOrder("test001");
        // 예외가 발생하는 경우를 가정
        assertThrows(Exception.class, () -> {
            orderDao.insertCurrentOrder("test001");
        });
    }

    @Test
    @DisplayName("주문 처리 내역 개수 조회 테스트")
    void testCountCurrentOrder() throws Exception {
        assertEquals(0, orderDao.countCurrentOrder());
        orderDao.insertCurrentOrder("test001");
        orderDao.insertCurrentOrder("test002");
        orderDao.insertCurrentOrder("test003");
        orderDao.insertCurrentOrder("test004");
        orderDao.insertCurrentOrder("test005");
        orderDao.insertCurrentOrder("test006");
        assertEquals(6, orderDao.countCurrentOrder());
        orderDao.deleteCurrentOrder("test002");
        assertEquals(5, orderDao.countCurrentOrder());
        CurrentOrderDTO testOrderResult = orderDao.getCurrentOrderById("test002");
        orderDao.insertCurrentOrder("test002");
        assertEquals(6, orderDao.countCurrentOrder());
    }

    @Test
    @DisplayName("주문 처리 내역 전체 삭제 테스트")
    void testcountCurrentOrderDeleteAllTest() throws Exception {
        assertEquals(0, orderDao.countCurrentOrder());
        orderDao.insertCurrentOrder("test001");
        assertEquals(1, orderDao.countCurrentOrder());
        assertEquals(1, orderDao.deleteAllCurrentOrder());
        assertEquals(0, orderDao.countCurrentOrder());
        orderDao.insertCurrentOrder("test002");
        assertEquals(1, orderDao.countCurrentOrder());
        orderDao.deleteCurrentOrder("test002");
        assertEquals(0, orderDao.countCurrentOrder());
    }

    @Test
    @DisplayName("주문 처리 상품 개수 조회 및 추가 테스트")
    void testCurrentOrderItem() throws Exception {
        assertEquals(0, orderDao.countCurrentOrder());
        orderDao.insertCurrentOrder("test001");
        orderDao.insertCurrentOrder("test002");
        orderDao.insertCurrentOrderItem("test001", 2588784L, 2L);
        assertEquals(1, orderDao.countCurrentOrderItem());
        orderDao.insertCurrentOrderItem("test002", 2588784L, 2L);
        assertEquals(2, orderDao.countCurrentOrderItem());
        assertEquals(1, orderDao.countUserCurrentOrderItemById("test001"));
        assertEquals(1, orderDao.countUserCurrentOrderItemById("test002"));
    }

    @Test
    @DisplayName("주문 처리 상품 삭제 테스트")
    void testDeleteAllCurrentOrderItem() throws Exception {
        assertEquals(0, orderDao.countCurrentOrder());
        orderDao.insertCurrentOrder("test001");
        orderDao.insertCurrentOrder("test002");
        orderDao.insertCurrentOrderItem("test001", 2588784L, 2L);
        orderDao.insertCurrentOrderItem("test002", 2588784L, 2L);
        assertEquals(2, orderDao.countCurrentOrderItem());
        assertEquals(1, orderDao.deleteUserCurrentOrderItem("test001", 2588784L));
        assertEquals(1, orderDao.countCurrentOrderItem());
        orderDao.insertCurrentOrderItem("test001", 2588784L, 2L);
        assertEquals(2, orderDao.deleteAllCurrentOrderItem());
        orderDao.insertCurrentOrderItem("test001", 2588784L, 2L);
        assertEquals(1, orderDao.deleteAllCurrentOrderItem());
    }

    // TODO : 작업 중
    @Test
    @DisplayName("주문용 배송지 추가 테스트")
    void testInsertOrderAddress() throws Exception {
        assertEquals(0, orderDao.countCurrentOrder());
        orderDao.insertCurrentOrder("test001");
        orderDao.insertCurrentOrder("test002");
        orderDao.insertCurrentOrderItem("test001", 2588784L, 2L);
        orderDao.insertCurrentOrderItem("test002", 2588784L, 2L);
        assertEquals(2, orderDao.countCurrentOrderItem());
        assertEquals(1, orderDao.deleteUserCurrentOrderItem("test001", 2588784L));
        assertEquals(1, orderDao.countCurrentOrderItem());
        orderDao.insertCurrentOrderItem("test001", 2588784L, 2L);
        assertEquals(2, orderDao.deleteAllCurrentOrderItem());
        orderDao.insertCurrentOrderItem("test001", 2588784L, 2L);
        assertEquals(1, orderDao.deleteAllCurrentOrderItem());
    }
}