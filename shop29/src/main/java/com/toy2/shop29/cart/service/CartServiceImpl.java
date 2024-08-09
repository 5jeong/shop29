package com.toy2.shop29.cart.service;

import com.toy2.shop29.cart.dao.CartDao;
import com.toy2.shop29.cart.domain.CartDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartDao cartDao;

    @Override
    public int checkUserCartExist(String userId) throws Exception {
        return cartDao.countUserCart(userId);
    }

    @Override
    public int getUserCartProductsCount(String userId) throws Exception {
        return cartDao.countUserCartProducts(userId);
    }

    // 유저 장바구니 생성
    @Override
    public int createUserCart(String userId, Integer is_user) throws Exception {
        return cartDao.createCart(userId, is_user);
    }

    // 특정 유저 장바구니 상품 모두 조회
    @Override
    public List<CartDto> getUserCartProducts(String userId, Integer is_user) throws Exception {
        return cartDao.findUserCartProductsByUserId(userId);
    }

    // 특정 유저 장바구니 상품 추가
    @Override
    public int addProductToCart(String userId, int productId, Integer quantity, Integer is_user) throws Exception {
        if (checkUserCartExist(userId) != 1) {
            createUserCart(userId, is_user);
        }
        CartDto specificProduct = cartDao.searchProductIdByUserIdAndProductId(userId, productId);
        if (specificProduct != null) {
            return updateProductQuantity(userId, productId, specificProduct.getQuantity() + quantity);
        }
        return cartDao.insertUserCartProduct(userId, productId, quantity);
    }

    @Override
    public int updateProductQuantity(String userId, int productId, int quantity) throws Exception {
        if (quantity <= 0) {
            return deleteSpecificProduct(userId, productId);
        }
        return cartDao.updateUserCartProductQuantity(userId, productId, quantity);
    }

    @Override
    public int deleteSpecificProduct(String userId, Integer productId) throws Exception {
        CartDto specificProduct = cartDao.searchProductIdByUserIdAndProductId(userId, productId);
        if (specificProduct == null) {
            return -1;
        }
        return cartDao.deleteUserCartProduct(userId, productId);
    }

    @Override
        public int updateGuestCartToUser(String userId, String guestId, Integer is_user) throws Exception {
        if (checkUserCartExist(guestId) != 1 || getUserCartProductsCount(guestId) == 0) {
            return 0;
        }
        List<CartDto> guestCartProducts = cartDao.findUserCartProductsByUserId(userId);
        for (CartDto cartDto : guestCartProducts) {
            addProductToCart(userId, cartDto.getProduct_id(), cartDto.getQuantity(), is_user);
        }
        return 1;
    }
}
