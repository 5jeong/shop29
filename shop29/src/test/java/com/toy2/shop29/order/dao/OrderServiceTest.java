package com.toy2.shop29.order.dao;

import com.toy2.shop29.order.domain.request.OrderProductDto;
import com.toy2.shop29.order.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OrderServiceTest {
    @Autowired
    private OrderService orderService;

    @BeforeEach
    public void setUp() throws Exception {
        orderService.deleteAllCurrentOrder();
        orderService.deleteAllCurrentOrderItem();
        orderService.deleteAllOrderHistory();
        orderService.deleteAllOrderHistoryItem();
    }

    @Test
    @DisplayName("주문 시 선택한 아이템이 주문 처리 테이블 추가 테스트")
    void testCurrentOrderCheck() throws Exception {
        assertEquals(0, orderService.countCurrentOrder());
        assertEquals(0, orderService.countCurrentOrderItem());

        String userId = "test001";

        OrderProductDto orderProduct1 = new OrderProductDto();
        OrderProductDto orderProduct2 = new OrderProductDto();
        OrderProductDto orderProduct3 = new OrderProductDto();
        orderProduct1.setProductId(1947442L);
        orderProduct1.setQuantity(1L);
        orderProduct2.setProductId(2032612L);
        orderProduct2.setQuantity(2L);
        orderProduct3.setProductId(2588784L);
        orderProduct3.setQuantity(3L);

        List<OrderProductDto> orderProducts = new ArrayList<>();
        orderProducts.add(orderProduct1);
        orderProducts.add(orderProduct2);
        orderProducts.add(orderProduct3);

        orderService.addProducts(userId, orderProducts);
        assertEquals(1, orderService.countCurrentOrder());
        assertEquals(3, orderService.countCurrentOrderItem());
    }

    @Test
    @DisplayName("주문 성공 시 주문 처리 내역 삭제하고 주문 내역에 추가되어야 함")
    void testOrderCompleted() throws Exception {
        assertEquals(0, orderService.countCurrentOrder());
        assertEquals(0, orderService.countCurrentOrderItem());

        String userId = "test001";

        OrderProductDto orderProduct1 = new OrderProductDto();
        OrderProductDto orderProduct2 = new OrderProductDto();
        OrderProductDto orderProduct3 = new OrderProductDto();
        orderProduct1.setProductId(1947442L);
        orderProduct1.setQuantity(1L);
        orderProduct2.setProductId(2032612L);
        orderProduct2.setQuantity(2L);
        orderProduct3.setProductId(2588784L);
        orderProduct3.setQuantity(3L);

        List<OrderProductDto> orderProducts = new ArrayList<>();
        orderProducts.add(orderProduct1);
        orderProducts.add(orderProduct2);
        orderProducts.add(orderProduct3);

        orderService.addProducts(userId, orderProducts);
        assertEquals(1, orderService.countCurrentOrder());
        assertEquals(3, orderService.countCurrentOrderItem());
    }
}
