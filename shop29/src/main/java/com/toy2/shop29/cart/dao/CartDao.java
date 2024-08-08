package com.toy2.shop29.cart.dao;

import com.toy2.shop29.cart.domain.CartDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface CartDao {

    // 유저 아이디로 장바구니 담은 상품 리스트 조회
    List<CartDto> findcartsByUserId(@Param("userId") String userId);

    // 같은 상품이 장바구니에 있는지 조회
    CartDto searchProductIdByUserIdAndProductId(@Param("userId") String userId, @Param("productId") String productId);

    // 장바구니 담기
    int insertCart(@Param("userId") String userId, @Param("productId") String productId);

    // 장바구니 수량 업데이트
    int updatecartQuantity(@Param("userId") String userId, @Param("productId") String productId, @Param("quantity") Integer quantity);

    // 장바구니 삭제
    int deleteCart(@Param("userId") String userId, @Param("productId") String productId);
}
