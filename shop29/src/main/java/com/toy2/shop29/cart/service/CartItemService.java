package com.toy2.shop29.cart.service;

import com.toy2.shop29.common.ProductItem;
import com.toy2.shop29.cart.domain.request.DeleteCartItemsRequestDto;

import java.util.List;

public interface CartItemService {
    void addProductToCart(String userId, ProductItem addCartProductDto, int isUser) throws Exception;

    void updateProductQuantity(String userId, ProductItem productItem) throws Exception;

    void deleteCartProducts(String userId, List<DeleteCartItemsRequestDto> productIds) throws Exception;

    void deleteSpecificProduct(String userId, Long productId, Long productOptionId) throws Exception;
}
