package com.toy2.shop29.cart.service;

import com.toy2.shop29.cart.domain.response.CartDto;

import java.util.List;

public interface CartService {
    // 본인 장바구니 존재한지 조회
    int checkUserCartExist(String userId) throws Exception;
    int getUserCartProductsCount(String userId) throws Exception;
    int createUserCart(String userId, int is_user) throws Exception;
    // 본인 장바구니 상품 조회
    List<CartDto> getUserCartProducts(String userId, int is_user) throws Exception;
    // 장바구니에 상품 추가
    int addProductToCart(String userId, Long productId, Long quantity, int is_user) throws Exception;
    int updateProductQuantity(String userId, Long productId, Long quantity) throws Exception;
    int deleteCartProducts(String userId, List<Long> productIds) throws Exception;
    void deleteSpecificProduct(String userId, Long productId) throws Exception;
    int updateGuestCartToUser(String userId, String guestId, int is_user) throws Exception;
    int updateCartLastUpdate(String userId) throws Exception;
}
