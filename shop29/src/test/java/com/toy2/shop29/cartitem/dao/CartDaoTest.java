package com.toy2.shop29.cartitem.dao;

import com.toy2.shop29.cart.dao.CartDaoImpl;
import com.toy2.shop29.cart.domain.CartDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CartDaoTest {
    @Autowired
    private CartDaoImpl cartDao;

    @Test
    @DisplayName("사용자의 전체 주문내역 조회")
    void testFindAllOrders() throws Exception {
        String testUserId = "user001";

        List<CartDto> result = cartDao.findcartsByUserId(testUserId);

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("장바구니 상품 추가")
    void testInsertcart() throws Exception {
        String testUserId = "user001";
        String productId = "5";

        // 장바구니 해당 상품 조회
        CartDto cart = cartDao.searchProductIdByUserIdAndProductId(testUserId, productId);
        assertThat(cart).isNull();

        // 장바구니에 해당 상품이 있어야 됨
        if (cart == null) {
            cartDao.insertCart(testUserId, productId);
            cart = cartDao.searchProductIdByUserIdAndProductId(testUserId, productId);
            assertThat(cart).isNotNull();
        }
    }

    @Test
    @DisplayName("장바구니 수량 더하기")
    void testIncreasecartQuantity() throws Exception {
        String testUserId = "user001";
        String productId = "5";
        int quantity = (int) (Math.random() * 11 + 10);

        // 장바구니 해당 상품 조회
        CartDto cart = cartDao.searchProductIdByUserIdAndProductId(testUserId, productId);

        // 장바구니에 해당 상품이 있어야 됨
        if (cart != null) {
            cartDao.updatecartQuantity(testUserId, productId, cart.getQuantity() + quantity);
            CartDto updatedcart = cartDao.searchProductIdByUserIdAndProductId(testUserId, productId);

            assertThat(updatedcart.getQuantity()).isGreaterThan(cart.getQuantity());
        }
    }

    @Test
    @DisplayName("장바구니 수량 빼기")
    void testDecreasecartQuantity() throws Exception {
        String testUserId = "user001";
        String productId = "5";
        int quantity = (int) (Math.random() * 11 + 10);

        // 장바구니 해당 상품 조회
        CartDto cart = cartDao.searchProductIdByUserIdAndProductId(testUserId, productId);

        // 장바구니에 해당 상품이 있어야 됨

        if (cart != null) {
            cartDao.updatecartQuantity(testUserId, productId, cart.getQuantity() - quantity);
            CartDto updatedcart = cartDao.searchProductIdByUserIdAndProductId(testUserId, productId);

            assertThat(updatedcart.getQuantity()).isLessThan(cart.getQuantity());
        }
    }

    @Test
    @DisplayName("장바구니 해당 상품 삭제")
    void testDeletecart() throws Exception {
        String testUserId = "user001";
        String productId = "5";

        CartDto cart = cartDao.searchProductIdByUserIdAndProductId(testUserId, productId);

        if (cart != null) {
            cartDao.deleteCart(testUserId, productId);
            cart = cartDao.searchProductIdByUserIdAndProductId(testUserId, productId);
            assertThat(cart).isNull();
        }

    }
}
