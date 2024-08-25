package com.toy2.shop29.cart.service;

import com.toy2.shop29.cart.domain.request.DeleteCartItemsRequestDto;
import com.toy2.shop29.cart.domain.response.CartDto;
import com.toy2.shop29.common.ProductItem;

import java.util.List;

public interface CartService {
    // 본인 장바구니 존재한지 조회
    int checkUserCartExist(String userId) throws Exception;

    int getUserCartProductsCount(String userId) throws Exception;

    int createUserCart(String userId, int is_user) throws Exception;

    // 본인 장바구니 상품 조회
    List<CartDto> getUserCartProducts(String userId, int is_user) throws Exception;

    // 장바구니에 상품 추가
    void addProductToCart(String userId, ProductItem addCartProductDto, int is_user) throws Exception;

    int updateProductQuantity(String userId, ProductItem productItem) throws Exception;

    int deleteCartProducts(String userId, List<DeleteCartItemsRequestDto> productIds) throws Exception;

    void deleteSpecificProduct(String userId, Long productId, Long productOptionId) throws Exception;

    void updateGuestCartToUser(String userId, String guestId, int is_user) throws Exception;

    int updateCartLastUpdate(String userId) throws Exception;

    List<ProductItem> selectUserCartItem(String userId) throws Exception;
}
