package com.toy2.shop29.cart.service;

import com.toy2.shop29.cart.dao.CartDao;
import com.toy2.shop29.cart.domain.response.CartDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartDao cartDao;

    // 해당 유저 장바구니 유무 확인
    @Override
    public int checkUserCartExist(String userId) throws Exception {
        return cartDao.countUserCart(userId);
    }

    // 해당 유저 장바구니 상품 개수 조회
    @Override
    public int getUserCartProductsCount(String userId) throws Exception {
        return cartDao.countUserCartProducts(userId);
    }

    // 해당 유저 장바구니 생성
    @Override
    public int createUserCart(String userId, int isUser) throws Exception {
        return cartDao.createCart(userId, isUser);
    }

    // 해당 유저 장바구니 상품정보 조회
    @Override
    public List<CartDto> getUserCartProducts(String userId, int isUser) throws Exception {
        return cartDao.selectUserCartProductsByUserId(userId);
    }

    // 해당 유저 장바구니에 상품 추가
    @Override
    @Transactional
    public int addProductToCart(String userId, Long productId, Long quantity, int isUser) throws Exception {
        ensureUserCartExists(userId, isUser);

        // TODO : 상품이 존재하는지 유무 확인
        if (false) {
            throw new IllegalArgumentException("존재하지 않는 상품 ID");
        }

        CartDto specificProduct = cartDao.searchProductIdByUserIdAndProductId(userId, productId);

        int result;
        if (specificProduct != null) {
            result = updateProductQuantity(userId, productId, specificProduct.getQuantity() + quantity);
        } else {
            result = cartDao.insertUserCartProduct(userId, productId, quantity);
            if (result > 0) {
                updateCartLastUpdate(userId);
            }
        }

        return result;
    }

    // 해당 유저 장바구니 상품 수량 수정
    @Override
    @Transactional
    public int updateProductQuantity(String userId, Long productId, Long quantity) throws Exception {
        // TODO : 상품이 존재하는지 유무 확인
        if (false) {
            throw new IllegalArgumentException("존재하지 않는 상품 ID");
        }

        if (quantity > 100) {
            throw new IllegalArgumentException("수량 초과");
        }

        if (quantity <= 0) {
            return deleteSpecificProduct(userId, productId);
        }

        int result = cartDao.updateUserCartProductQuantity(userId, productId, quantity);
        if (result > 0) {
            updateCartLastUpdate(userId);
        }

        return result;
    }

    // 선택 상품들 삭제
    @Override
    @Transactional
    public int deleteCartProducts(String userId, List<Long> productIds) throws Exception {
        for (Long productId : productIds) {
            deleteSpecificProduct(userId, productId);
        }
        return updateCartLastUpdate(userId);
    }

    // 상품 삭제
    @Override
    @Transactional
    public int deleteSpecificProduct(String userId, Long productId) throws Exception {
        CartDto specificProduct = cartDao.searchProductIdByUserIdAndProductId(userId, productId);
        if (specificProduct == null) {
            return -1;
        }
        int result = cartDao.deleteUserCartProduct(userId, productId);
        if (result > 0) {
            updateCartLastUpdate(userId);
        }
        return result;
    }

    // 장바구니에 담은 상태에서 로그인 할 경우 장바구니 이전
    // TODO : 테스트 필요
    @Override
    @Transactional
    public int updateGuestCartToUser(String userId, String guestId, int isUser) throws Exception {
        if (checkUserCartExist(guestId) != 1 || getUserCartProductsCount(guestId) == 0) {
            return 0;
        }
        List<CartDto> guestCartProducts = cartDao.selectUserCartProductsByUserId(guestId);
        for (CartDto cartDto : guestCartProducts) {
            addProductToCart(userId, cartDto.getProductId(), cartDto.getQuantity(), isUser);
        }
        int result = cartDao.deleteUserCart(guestId);

        return 1;
    }

    public int updateCartLastUpdate(String userId) throws Exception {
        return cartDao.updateCartLastUpdate(userId);
    }

    /**
     * 장바구니가 없는 경우 생성하는 로직
     */
    private void ensureUserCartExists(String userId, Integer isUser) throws Exception {
        if (checkUserCartExist(userId) != 1) {
            createUserCart(userId, isUser);
        }
    }
}
