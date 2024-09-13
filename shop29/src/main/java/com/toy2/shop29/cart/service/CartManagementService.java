package com.toy2.shop29.cart.service;

import com.toy2.shop29.cart.domain.response.CartDto;
import com.toy2.shop29.common.ProductItem;

import java.util.List;

public interface CartManagementService {
    int checkUserCartExist(String userId) throws Exception;

    List<CartDto> getUserCartProducts(String userId, int isUser) throws Exception;

    List<ProductItem> selectUserCartItem(String userId) throws Exception;

    int getUserCartProductsCount(String userId) throws Exception;

    int updateCartLastUpdate(String userId) throws Exception;
}
