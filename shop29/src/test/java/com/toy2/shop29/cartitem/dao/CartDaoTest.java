package com.toy2.shop29.cartitem.dao;

import com.toy2.shop29.cart.dao.CartDaoImpl;
import com.toy2.shop29.cart.domain.CartDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CartDaoTest {
    @Autowired
    private CartDaoImpl cartDao;

    @Test
    @DisplayName("사용자 장바구니 추가")
    void testCreateCart() throws Exception {
        String testUserId = "user001";

        int result = cartDao.countUserCart(testUserId);

        assertThat(result).isEqualTo(1);
    }

    @Test
    @DisplayName("장바구니 상품 추가")
    void testInsertCart() throws Exception {
        String testUserId = "user001";
        Integer productId = 5;
        Integer quantity = 5;

        // 장바구니 해당 상품 조회
        CartDto cart = cartDao.searchProductIdByUserIdAndProductId(testUserId, productId);
        assertThat(cart).isNull();

        try {
            cartDao.createCart(testUserId, 1);
        }
        catch (Exception e) {
            System.out.println(e);
        }
        // 장바구니에 해당 상품이 있어야 됨
        if (cart == null) {
            cartDao.insertUserCartProduct(testUserId, productId, quantity);
            cart = cartDao.searchProductIdByUserIdAndProductId(testUserId, productId);
            assertThat(cart).isNotNull();
        }
    }

    @Test
    @DisplayName("장바구니 수량 더하기")
    void testIncreaseUserCartProductQuantity() throws Exception {
        String testUserId = "user001";
        Integer productId = 5;
        int quantity = (int) (Math.random() * 11 + 10);

        // 장바구니 해당 상품 조회
        CartDto cart = cartDao.searchProductIdByUserIdAndProductId(testUserId, productId);

        // 장바구니에 해당 상품이 있어야 됨
        if (cart != null) {
            cartDao.updateUserCartProductQuantity(testUserId, productId, cart.getQuantity() + quantity);
            CartDto updater = cartDao.searchProductIdByUserIdAndProductId(testUserId, productId);

            assertThat(updater.getQuantity()).isGreaterThan(cart.getQuantity());
        }
    }

    @Test
    @DisplayName("장바구니 수량 빼기")
    void testDecreaseUserCartProductQuantity() throws Exception {
        String testUserId = "user001";
        Integer productId = 5;
        int quantity = (int) (Math.random() * 11 + 10);

        // 장바구니 해당 상품 조회
        CartDto cart = cartDao.searchProductIdByUserIdAndProductId(testUserId, productId);

        // 장바구니에 해당 상품이 있어야 됨

        if (cart != null) {
            cartDao.updateUserCartProductQuantity(testUserId, productId, cart.getQuantity() - quantity);
            CartDto updatedcart = cartDao.searchProductIdByUserIdAndProductId(testUserId, productId);

            assertThat(updatedcart.getQuantity()).isLessThan(cart.getQuantity());
        }
    }

    @Test
    @DisplayName("장바구니 해당 상품 삭제")
    void testDeleteUserCartProduct() throws Exception {
        String testUserId = "user001";
        Integer productId = 5;

        CartDto cart = cartDao.searchProductIdByUserIdAndProductId(testUserId, productId);

        if (cart != null) {
            cartDao.deleteUserCartProduct(testUserId, productId);
            cart = cartDao.searchProductIdByUserIdAndProductId(testUserId, productId);
            assertThat(cart).isNull();
        }
    }

    @Test
    @DisplayName("유저의 장바구니 삭제")
    void testDeleteCart() throws Exception {
        String testUserId = "user001";

        cartDao.deleteUserCart(testUserId);

        assertThat(cartDao.countUserCart(testUserId)).isEqualTo(0);
        assertThat(cartDao.countUserCartProducts(testUserId)).isEqualTo(0);
    }
}
