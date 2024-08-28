package com.toy2.shop29.cart.dao;

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
        assertEquals(0, cartDao.countCart(), "장바구니 개수가 0이어야 함");
        assertEquals(0, cartDao.countCartItem(),"장바구니 아이템의 개수가 0이어야 함");
    }

    @Test
    @DisplayName("장바구니 추가 테스트")
    void testInsertCart() throws Exception {
        assertEquals(0, cartDao.countCart(), "초기 장바구니 개수는 0이어야 함");
        cartDao.createCart("test001", 1);
        // 예외가 발생하는 경우를 가정
        assertThrows(Exception.class, () -> {
            cartDao.createCart("test001", 1);
        },"동일한 유저의 장바구니를 생성하면 예외가 발생해야 함");
    }

    @Test
    @DisplayName("장바구니 개수 조회 테스트")
    void testCountCart() throws Exception {
        // 초기 상태에서 장바구니 개수는 0이어야 함
        assertEquals(0, cartDao.countCart(), "초기 장바구니 개수는 0이어야 함");

        // 새로운 유저의 장바구니 추가
        cartDao.createCart("test001", 1);
        cartDao.createCart("test002", 0);
        cartDao.createCart("test003", 0);
        cartDao.createCart("test004", 0);
        cartDao.createCart("test005", 0);
        cartDao.createCart("test006", 0);

        // 유저 test001의 장바구니 삭제
        assertEquals(1, cartDao.deleteUserCart("test001"), "유저 test001의 장바구니 삭제는 성공해야 함");

        // 삭제 후 장바구니 개수는 5개여야 함
        assertEquals(5, cartDao.countCart(), "유저 test001 삭제 후 장바구니 개수는 5이어야 함");

        // 유저 test001의 장바구니 재생성
        cartDao.createCart("test001", 1);

        // 재생성 후 장바구니 개수는 6개여야 함
        assertEquals(6, cartDao.countCart(), "유저 test001 재생성 후 장바구니 개수는 6이어야 함");
    }


    @Test
    @DisplayName("장바구니 전체 삭제 테스트")
    void testDeleteAllCart() throws Exception {
        // 초기 상태에서 장바구니 개수는 0이어야 함
        assertEquals(0, cartDao.countCart(), "초기 장바구니 개수는 0이어야 함");

        // 여러 사용자의 장바구니 생성
        cartDao.createCart("test001", 1);
        cartDao.createCart("test002", 0);
        cartDao.createCart("test003", 0);
        cartDao.createCart("test004", 0);
        cartDao.createCart("test005", 0);
        cartDao.createCart("test006", 0);

        // 모든 장바구니 삭제
        assertEquals(6, cartDao.deleteCart(), "모든 장바구니가 삭제되어야 함");

        // 삭제 후 장바구니 개수는 0이어야 함
        assertEquals(0, cartDao.countCart(), "모든 장바구니 삭제 후 개수는 0이어야 함");

        // 새로운 장바구니 추가 후 삭제
        cartDao.createCart("test001", 1);
        assertEquals(1, cartDao.deleteCart(), "하나의 장바구니가 삭제되어야 함");

        // 최종적으로 장바구니 개수는 0이어야 함
        assertEquals(0, cartDao.countCart(), "최종적으로 장바구니 개수는 0이어야 합니함");
    }


    @Test
    @DisplayName("장바구니 상품 개수 및 추가, 삭제 테스트")
    void testCartItem() throws Exception {
        // 초기 상태에서 장바구니 상품 개수는 0이어야 함
        assertEquals(0, cartDao.countCartItem(), "초기 장바구니 상품 개수는 0이어야 함");

        // 장바구니가 없을 때 상품을 추가하면 예외가 발생해야 함
        assertThrows(Exception.class, () -> {
            cartDao.insertUserCartProduct("test001", 5L, 1L, 23L);
        }, "장바구니가 없을 때 상품을 추가하면 예외가 발생해야 함");

        // 새로운 유저들의 장바구니 생성
        cartDao.createCart("test001", 1);
        cartDao.createCart("test002", 0);
        cartDao.createCart("test003", 0);
        cartDao.createCart("test004", 0);
        cartDao.createCart("test005", 0);
        cartDao.createCart("test006", 0);
        cartDao.createCart("test007", 0);

        // 각 유저의 장바구니에 상품 추가
        cartDao.insertUserCartProduct("test001", 5L, 1L, 23L);
        cartDao.insertUserCartProduct("test002", 5L, 1L, 23L);
        cartDao.insertUserCartProduct("test003", 5L, 1L, 23L);
        cartDao.insertUserCartProduct("test004", 5L, 1L, 23L);
        cartDao.insertUserCartProduct("test005", 5L, 1L, 23L);
        cartDao.insertUserCartProduct("test006", 5L, 1L, 23L);

        // 현재 장바구니에 6개의 상품이 추가되었는지 확인
        assertEquals(6, cartDao.countCartItem(), "6개의 상품이 장바구니에 있어야 함");

        // 추가로 test007의 장바구니에 상품 추가
        cartDao.insertUserCartProduct("test007", 5L, 1L, 23L);

        // 최종적으로 장바구니에 7개의 상품이 있어야 함
        assertEquals(7, cartDao.countCartItem(), "7개의 상품이 최종적으로 장바구니에 있어야 함.");
    }
}
