package com.toy2.shop29.cart.service;

import com.toy2.shop29.cart.dao.CartDao;
import com.toy2.shop29.cart.domain.response.CartDto;
import com.toy2.shop29.common.ProductItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartManagementServiceImpl implements CartManagementService {
    private final CartDao cartDao;

    /**
     * 사용자의 장바구니가 존재하는지 확인하는 메서드
     *
     * @param userId 사용자의 UID (로그인 여부와 무관)
     * @return 장바구니가 존재하면 1, 존재하지 않으면 0 반환
     * @throws Exception 확인 중 오류가 발생할 경우
     */
    @Override
    public int checkUserCartExist(String userId) throws Exception {
        return cartDao.countUserCart(userId);
    }

    /**
     * 사용자의 장바구니에 담긴 상품의 상세 정보를 조회하는 메서드
     *
     * @param userId 사용자의 UID (로그인 여부와 무관)
     * @param isUser 로그인 여부를 나타내는 플래그 (1이면 로그인, 0이면 비로그인)
     * @return 장바구니에 담긴 상품의 상세 정보 목록
     * @throws Exception 조회 중 오류가 발생할 경우
     */
    @Override
    public List<CartDto> getUserCartProducts(String userId, int isUser) throws Exception {
        return cartDao.selectUserCartProductsByUserId(userId);
    }

    /**
     * 사용자의 장바구니에 담긴 상품 목록을 조회하는 메서드
     *
     * @param userId 사용자의 UID (로그인 여부와 무관)
     * @return 사용자의 장바구니에 담긴 상품 목록
     * @throws Exception 조회 중 오류가 발생할 경우
     */
    @Override
    public List<ProductItem> selectUserCartItem(String userId) throws Exception {
        return cartDao.selectUserCartItem(userId);
    }

    @Override
    public int getUserCartProductsCount(String userId) throws Exception {
        return cartDao.countUserCartProducts(userId);
    }

    /**
     * 사용자의 장바구니 마지막 수정일(lastUpdate)을 업데이트하는 메서드
     *
     * @param userId 사용자의 UID (로그인 여부와 무관)
     * @return 업데이트 성공 시 1, 실패 시 0 반환
     * @throws Exception 업데이트 중 오류가 발생할 경우
     */
    public int updateCartLastUpdate(String userId) throws Exception {
        return cartDao.updateCartLastUpdate(userId);
    }
}
