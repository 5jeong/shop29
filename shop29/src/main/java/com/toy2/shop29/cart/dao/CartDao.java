package com.toy2.shop29.cart.dao;

import com.toy2.shop29.cart.domain.CartDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface CartDao {
    int countAllUsersCart() throws Exception;
    
    int countUserCartProducts(@Param("userId") String userId) throws Exception;

    // 유저의 장바구니 유무 조회
    int countUserCart(@Param("userId") String userId) throws Exception;
    
    // 장바구니 생성
    int createCart(@Param("userId") String userId, @Param("is_user") Integer is_user) throws Exception;
    
    // 유저 아이디로 장바구니 담은 상품 리스트 조회
    List<CartDto> findUserCartProductsByUserId(@Param("userId") String userId) throws Exception;

    // 같은 상품이 장바구니에 있는지 조회
    CartDto searchProductIdByUserIdAndProductId(@Param("userId") String userId, @Param("productId") Integer productId) throws Exception;

    // 유저 장바구니 상품 담기
    int insertUserCartProduct(@Param("userId") String userId, @Param("productId") Integer productId, @Param("quantity") Integer quantity) throws Exception;

    // 장바구니 마지막 수정일 업데이트
    int modifyCartLastUpdate(@Param("userId") String userId) throws Exception;

    // 유저 장바구니 상품 수량 업데이트
    int updateUserCartProductQuantity(@Param("userId") String userId, @Param("productId") Integer productId, @Param("quantity") Integer quantity) throws Exception;

    // 유저 장바구니 상품 삭제
    int deleteUserCartProduct(@Param("userId") String userId, @Param("productId") Integer productId) throws Exception;

    // 유저 장바구니 삭제
    int deleteUserCart(@Param("userId") String userId) throws Exception;
}
