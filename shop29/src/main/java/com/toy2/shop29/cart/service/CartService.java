package com.toy2.shop29.cart.service;

import com.toy2.shop29.cart.domain.CartDto;

import java.util.List;

public interface CartService {
    // 본인 장바구니 존재한지 조회
    int checkUserCartExist(String userId) throws Exception;
    int getUserCartProductsCount(String userId) throws Exception;
    int createUserCart(String userId, Integer is_user) throws Exception;
    // 본인 장바구니 상품 조회
    List<CartDto> getUserCartProducts(String userId, Integer is_user) throws Exception;
    // 장바구니에 상품 추가
    int addProductToCart(String userId, int productId, Integer quantity, Integer is_user) throws Exception;
    int updateProductQuantity(String userId, int productId, int quantity) throws Exception;
    int deleteSpecificProduct(String userId, Integer productId) throws Exception;
    int updateGuestCartToUser(String userId, String guestId, Integer is_user) throws Exception;
    // int deleteUserCart(String userId) throws Exception;
}
