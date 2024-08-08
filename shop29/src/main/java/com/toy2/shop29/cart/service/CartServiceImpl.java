package com.toy2.shop29.cart.service;

import com.toy2.shop29.cart.dao.CartDao;
import com.toy2.shop29.cart.domain.CartDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    CartDao cartDao;

    @Override
    public int addCart(String userID, String productID) throws Exception {
        return cartDao.insertCart(userID, productID);
    }

    @Override
    public int removeCart(String userID, String productID) throws Exception {
        return cartDao.deleteCart(userID, productID);
    }

    @Override
    public int updateCart(String userID, String productID, Integer quantity) throws Exception {
        return cartDao.updatecartQuantity(userID, productID, quantity);
    }

    @Override
    public List<CartDto> getAllCart(String userId) throws Exception {
        return cartDao.findcartsByUserId(userId);
    }
}
