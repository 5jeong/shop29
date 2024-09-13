package com.toy2.shop29.cart.service;

import com.toy2.shop29.cart.dao.CartDao;
import com.toy2.shop29.cart.domain.request.DeleteCartItemsRequestDto;
import com.toy2.shop29.cart.domain.response.CartDto;
import com.toy2.shop29.common.ProductItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CartServiceTest {
    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private CartManagementService cartManagementService;

    @Autowired
    private CartMergeService cartMergeService;

    @Autowired
    private CartDao cartDao;

    @BeforeEach
    public void setUp() throws Exception {
        cartDao.deleteCart();
        cartDao.deleteCartItem();
        assertEquals(0, cartDao.countCart(), "장바구니 개수가 0이어야 함");
        assertEquals(0, cartDao.countCartItem(), "장바구니 이이템의 개수가 0이어야 함");
        cartDao.createCart("test001", 1);
        cartDao.createCart("test002", 0);
        assertEquals(2, cartDao.countCart(), "장바구니 개수가 2이어야 함");
        assertEquals(0, cartDao.countCartItem(), "장바구니 이이템의 개수가 0이어야 함");
    }

    @Test
    @DisplayName("상품 추가 및 조회")
    void addProductToCart() throws Exception {
        String user1 = "test001";
        String user2 = "test002";
        int cartProducts1 = cartManagementService.getUserCartProducts(user1, 1).size();
        int cartProducts2 = cartManagementService.getUserCartProducts(user2, 0).size();
        assertEquals(0, cartProducts1, "test001 유저의 장바구니 상품 개수가 0이어야 함");
        assertEquals(0, cartProducts2, "test002 유저의 장바구니 상품 개수가 0이어야 함");
        ProductItem productItem = new ProductItem();
        productItem.setProductId(2037819L);
        productItem.setProductOptionId(27L);
        productItem.setQuantity(2L);
        cartItemService.addProductToCart(user1, productItem, 1);
        List<CartDto> userCartItems = cartManagementService.getUserCartProducts(user1, 1);
        assertEquals(1, userCartItems.size());
        assertEquals(productItem.getProductId(), userCartItems.get(0).getProductId());
        assertEquals(productItem.getQuantity(), userCartItems.get(0).getQuantity());
        assertEquals(productItem.getProductOptionId(), userCartItems.get(0).getProductOptionId());
    }

    @Test
    @DisplayName("상품을 계속 추가할 경우 동일한 상품의 개수만 추가되어야 함")
    void addProductToCart2() throws Exception {
        String user1 = "test001";
        int cartProducts1 = cartManagementService.getUserCartProducts(user1, 1).size();
        assertEquals(0, cartProducts1, "test001 유저의 장바구니 상품 개수가 0이어야 함");
        ProductItem productItem = new ProductItem();
        productItem.setProductId(2037819L);
        productItem.setProductOptionId(27L);
        productItem.setQuantity(2L);
        cartItemService.addProductToCart(user1, productItem, 1);
        CartDto specificProduct1 = cartDao.searchProductIdByUserIdAndProductId(user1, productItem.getProductId(), productItem.getProductOptionId());
        assertEquals(productItem.getQuantity(), specificProduct1.getQuantity(), "장바구니 특정 상품 개수가 2개여야 함");
        productItem.setQuantity(3L);
        cartItemService.addProductToCart(user1, productItem, 1);
        CartDto specificProduct2 = cartDao.searchProductIdByUserIdAndProductId(user1, productItem.getProductId(), productItem.getProductOptionId());
        assertEquals(productItem.getQuantity() + specificProduct1.getQuantity(), specificProduct2.getQuantity(), "장바구니 특정 상품 개수가 5여야 함");
    }

    @Test
    @DisplayName("상품 개수가 원하는 수량만큼으로 조정되어야 함")
    void changeQuantitySpecificProduct() throws Exception {
        String user1 = "test001";
        int cartProducts1 = cartManagementService.getUserCartProducts(user1, 1).size();
        assertEquals(0, cartProducts1, "test001 유저의 장바구니 상품 개수가 0이어야 함");
        ProductItem productItem = new ProductItem();
        productItem.setProductId(2037819L);
        productItem.setProductOptionId(27L);
        productItem.setQuantity(2L);
        cartItemService.addProductToCart(user1, productItem, 1);
        CartDto specificProduct = cartDao.searchProductIdByUserIdAndProductId(user1, productItem.getProductId(), productItem.getProductOptionId());
        assertEquals(productItem.getQuantity(), specificProduct.getQuantity(), "특정 상품 개수가 2개여야 함");
        productItem.setQuantity(3L);
        cartItemService.updateProductQuantity(user1, productItem);
        specificProduct = cartDao.searchProductIdByUserIdAndProductId(user1, productItem.getProductId(), productItem.getProductOptionId());
        assertEquals(productItem.getQuantity(), specificProduct.getQuantity(), "특정 상품의 개수가 3이여야 함");
        productItem.setQuantity(5L);
        cartItemService.updateProductQuantity(user1, productItem);
        specificProduct = cartDao.searchProductIdByUserIdAndProductId(user1, productItem.getProductId(), productItem.getProductOptionId());
        assertEquals(productItem.getQuantity(), specificProduct.getQuantity(), "특정 상품의 개수가 5이여야 함");
    }

    @Test
    @DisplayName("특정 상품이 삭제되어야 함")
    void removeProductFromCart() throws Exception {
        String user1 = "test001";
        int cartProducts1 = cartManagementService.getUserCartProducts(user1, 1).size();
        assertEquals(0, cartProducts1, "test001 유저의 장바구니 상품 개수가 0이어야 함");

        ProductItem productItem1 = new ProductItem();
        productItem1.setProductId(2037819L);
        productItem1.setProductOptionId(27L);
        productItem1.setQuantity(2L);

        ProductItem productItem2 = new ProductItem();
        productItem2.setProductId(2644222L);
        productItem2.setProductOptionId(3L);
        productItem2.setQuantity(2L);

        cartItemService.addProductToCart(user1, productItem1, 1);
        cartItemService.addProductToCart(user1, productItem2, 1);

        List<CartDto> userCartItems = cartManagementService.getUserCartProducts(user1, 1);
        assertEquals(2, userCartItems.size(), "유저의 상품 개수가 2여야 함");

        DeleteCartItemsRequestDto deleteCartItemsRequestDto = new DeleteCartItemsRequestDto();
        deleteCartItemsRequestDto.setProductId(2037819L);
        deleteCartItemsRequestDto.setProductOptionId(27L);
        List<DeleteCartItemsRequestDto> deleteCartItemsList = new ArrayList<>();
        deleteCartItemsList.add(deleteCartItemsRequestDto);
        cartItemService.deleteCartProducts(user1, deleteCartItemsList);

        userCartItems = cartManagementService.getUserCartProducts(user1, 1);
        assertEquals(1, userCartItems.size(), "유저의 상품 개수가 1이여야 함");

        cartItemService.addProductToCart(user1, productItem1, 1);
        userCartItems = cartManagementService.getUserCartProducts(user1, 1);
        assertEquals(2, userCartItems.size(), "유저의 상품 개수가 2여야 함");

        cartItemService.deleteSpecificProduct(user1, productItem2.getProductId(), productItem2.getProductOptionId());

        userCartItems = cartManagementService.getUserCartProducts(user1, 1);
        assertEquals(1, userCartItems.size(), "유저의 상품 개수가 1이여야 함");

        cartDao.deleteUserCart(user1);
        userCartItems = cartManagementService.getUserCartProducts(user1, 0);
        assertEquals(0, userCartItems.size(), "유저의 상품 개수가 0이여야 함");
    }

    @Test
    @DisplayName("비로그인 유저의 장바구니 아이템이 로그인 시 유저 장바구니로 마이그레이션 되어야 함")
    void mergeCartProducts() throws Exception {
        String user1 = "test001";
        String guest1 = "guest001";
        List<CartDto> guestCartItems = cartManagementService.getUserCartProducts(guest1, 0);
        assertEquals(0, guestCartItems.size(), "비로그인 유저의 상품 개수가 0이여야 함");
        List<CartDto> userCartItems = cartManagementService.getUserCartProducts(user1, 1);
        assertEquals(0, userCartItems.size(), "로그인 유저의 상품 개수가 0이여야 함");
        cartDao.createCart(guest1, 0);


        ProductItem productItem1 = new ProductItem();
        productItem1.setProductId(2037819L);
        productItem1.setProductOptionId(27L);
        productItem1.setQuantity(2L);

        ProductItem productItem2 = new ProductItem();
        productItem2.setProductId(2644222L);
        productItem2.setProductOptionId(3L);
        productItem2.setQuantity(2L);

        cartItemService.addProductToCart(guest1, productItem1, 0);
        cartItemService.addProductToCart(guest1, productItem2, 0);

        guestCartItems = cartManagementService.getUserCartProducts(guest1, 0);
        assertEquals(2, guestCartItems.size(), "비로그인 유저의 상품 개수가 2여야 함");
        userCartItems = cartManagementService.getUserCartProducts(user1, 1);
        assertEquals(0, userCartItems.size(), "로그인 유저의 상품 개수가 0이여야 함");

        cartMergeService.updateGuestCartToUser(user1, guest1);

        guestCartItems = cartManagementService.getUserCartProducts(guest1, 0);
        assertEquals(0, guestCartItems.size(), "비로그인 유저의 상품 개수가 0이여야 함");
        userCartItems = cartManagementService.getUserCartProducts(user1, 1);
        assertEquals(2, userCartItems.size(), "로그인 유저의 상품 개수가 2여야 함");
    }
}
