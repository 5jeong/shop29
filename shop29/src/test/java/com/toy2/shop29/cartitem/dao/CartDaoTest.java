package com.toy2.shop29.cartitem.dao;

import com.toy2.shop29.cart.dao.CartDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CartDaoTest {
    @Autowired
    private CartDao cartDao;

    @BeforeEach
    void setUp() throws Exception {
        cartDao.deleteCart();
        cartDao.deleteCartItem();
    }

    @Test
    @DisplayName("장바구니 추가 테스트")
    void testInsertCart() throws Exception {
        assertEquals(0, cartDao.countCart());
        cartDao.createCart("test001", 1);
        // 예외가 발생하는 경우를 가정
        assertThrows(Exception.class, () -> {
            cartDao.createCart("test001", 1);
        });
    }

    @Test
    @DisplayName("장바구니 개수 조회 테스트")
    void testCountCart() throws Exception {
        assertEquals(0, cartDao.countCart());
        cartDao.createCart("test001", 1);
        cartDao.createCart("test002", 0);
        cartDao.createCart("test003", 0);
        cartDao.createCart("test004", 0);
        cartDao.createCart("test005", 0);
        cartDao.createCart("test006", 0);
        assertEquals(1, cartDao.deleteUserCart("test001"));
        assertEquals(5, cartDao.countCart());
        cartDao.createCart("test001", 1);
        assertEquals(6, cartDao.countCart());
    }

    @Test
    @DisplayName("장바구니 전체 삭제 테스트")
    void testDeleteAllCart() throws Exception {
        assertEquals(0, cartDao.countCart());
        cartDao.createCart("test001", 1);
        cartDao.createCart("test002", 0);
        cartDao.createCart("test003", 0);
        cartDao.createCart("test004", 0);
        cartDao.createCart("test005", 0);
        cartDao.createCart("test006", 0);
        assertEquals(6, cartDao.deleteCart());
        assertEquals(0, cartDao.countCart());
        cartDao.createCart("test001", 1);
        assertEquals(1, cartDao.deleteCart());
        assertEquals(0, cartDao.countCart());
    }

    @Test
    @DisplayName("장바구니 상품 개수 및 추가, 삭제 테스트")
    void testCartItem() throws Exception {
        assertEquals(0, cartDao.countCartItem());

        assertThrows(Exception.class, () -> {
            cartDao.insertUserCartProduct("test001", 5L, 1L);
        });

        cartDao.createCart("test001", 1);
        cartDao.createCart("test002", 0);
        cartDao.createCart("test003", 0);
        cartDao.createCart("test004", 0);
        cartDao.createCart("test005", 0);
        cartDao.createCart("test006", 0);
        cartDao.createCart("test007", 0);
        cartDao.insertUserCartProduct("test001", 5L, 1L);
        cartDao.insertUserCartProduct("test002", 5L, 1L);
        cartDao.insertUserCartProduct("test003", 5L, 1L);
        cartDao.insertUserCartProduct("test004", 5L, 1L);
        cartDao.insertUserCartProduct("test005", 5L, 1L);
        cartDao.insertUserCartProduct("test006", 5L, 1L);

        assertEquals(6, cartDao.countCartItem());

        cartDao.insertUserCartProduct("test007", 5L, 1L);

        assertEquals(7, cartDao.countCartItem());
    }
}
