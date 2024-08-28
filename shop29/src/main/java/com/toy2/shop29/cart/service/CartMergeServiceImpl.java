package com.toy2.shop29.cart.service;

import com.toy2.shop29.cart.dao.CartDao;
import com.toy2.shop29.cart.exception.CartNotFoundException;
import com.toy2.shop29.common.ProductItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartMergeServiceImpl implements CartMergeService {
    private final CartDao cartDao;
    private final CartItemService cartItemService;
    private final CartManagementService cartManagementService;

    /**
     * 비로그인 사용자가 담은 장바구니를 로그인 사용자에게 이전하는 메서드
     *
     * @param loginUser 로그인한 사용자의 UID
     * @param guestId   비로그인 사용자의 UID
     * @throws Exception 장바구니 이전 중 오류가 발생할 경우
     */
    @Override
    @Transactional
    public void updateGuestCartToUser(String loginUser, String guestId) throws Exception {
        if (!isCartTransferNeeded(guestId)) {
            return;
        }

        List<ProductItem> guestCartProducts = cartManagementService.selectUserCartItem(guestId);
        for (ProductItem cartDto : guestCartProducts) {
            cartItemService.addProductToCart(loginUser, cartDto, 1);
        }
        if (removeGuestCart(guestId) == 0) {
            throw new CartNotFoundException("해당 유저의 장바구니를 찾을 수 없음");
        }
    }

    /**
     * 비로그인 사용자의 장바구니가 로그인 사용자에게 이전이 필요한지 확인하는 메서드
     *
     * @param guestId 비로그인 사용자의 UID
     * @return 장바구니 이전이 필요하면 true, 그렇지 않으면 false 반환
     * @throws Exception 확인 중 오류가 발생할 경우
     */
    private boolean isCartTransferNeeded(String guestId) throws Exception {
        return isGuestCartNotEmpty(guestId) && cartManagementService.getUserCartProductsCount(guestId) > 0;
    }

    /**
     * 비로그인 사용자의 장바구니가 비어 있지 않은지 확인하는 메서드
     *
     * @param guestId 비로그인 사용자의 UID
     * @return 장바구니가 비어 있지 않으면 true, 비어 있으면 false 반환
     * @throws Exception 확인 중 오류가 발생할 경우
     */
    private boolean isGuestCartNotEmpty(String guestId) throws Exception {
        return cartManagementService.checkUserCartExist(guestId) == 1;
    }

    /**
     * 비로그인 사용자의 장바구니를 삭제하는 메서드
     *
     * @param guestId 비로그인 사용자의 UID
     * @return 삭제된 장바구니의 개수 반환
     * @throws Exception 삭제 중 오류가 발생할 경우
     */
    private int removeGuestCart(String guestId) throws Exception {
        return cartDao.deleteUserCart(guestId);
    }
}
