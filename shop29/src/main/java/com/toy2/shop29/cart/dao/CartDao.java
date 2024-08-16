package com.toy2.shop29.cart.dao;

import com.toy2.shop29.cart.domain.response.CartDto;

import java.util.List;


public interface CartDao {
    int countCart() throws Exception;

    int countUserCartProducts(String userId) throws Exception;

    // 유저의 장바구니 유무 조회
    int countUserCart(String userId) throws Exception;

    // 장바구니 생성
    int createCart(String userId, int isUser) throws Exception;

    // 유저 아이디로 장바구니 담은 상품 리스트 조회
    List<CartDto> selectUserCartProductsByUserId(String userId) throws Exception;

    // 같은 상품이 장바구니에 있는지 조회
    CartDto searchProductIdByUserIdAndProductId(String userId, Long productId) throws Exception;

    // 유저 장바구니 상품 담기
    int insertUserCartProduct(String userId, Long productId, Long quantity) throws Exception;

    // 장바구니 마지막 수정일 업데이트
    int updateCartLastUpdate(String userId) throws Exception;

    // 유저 장바구니 상품 수량 업데이트
    int updateUserCartProductQuantity(String userId, Long productId, Long quantity) throws Exception;

    // 유저 장바구니 상품 삭제
    int deleteUserCartProduct(String userId, Long productId) throws Exception;

    // 유저 장바구니 삭제
    int deleteUserCart(String userId) throws Exception;

    int deleteCart() throws Exception;

    int deleteCartItem() throws Exception;

    int countCartItem() throws Exception;
}
