package com.toy2.shop29.cart.service;

import com.toy2.shop29.cart.domain.CartDto;

import java.util.List;

public interface CartService {
    int addCart(String userID, String productID) throws Exception;
    int removeCart(String userID, String productID) throws Exception;
    int updateCart(String userID, String productID, Integer quantity) throws Exception;
    List<CartDto> getAllCart(String userId) throws Exception;
}
