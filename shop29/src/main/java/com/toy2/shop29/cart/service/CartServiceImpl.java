package com.toy2.shop29.cart.service;

import com.toy2.shop29.cart.dao.CartDao;
import com.toy2.shop29.cart.domain.response.CartDto;
import com.toy2.shop29.cart.exception.CartNotFoundException;
import com.toy2.shop29.product.dao.ProductDao;
import com.toy2.shop29.product.domain.ProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartDao cartDao;

    @Autowired
    private ProductDao productDao;

    /**
     * 해당 유저 장바구니 유무 확인
     *
     * @param userId 유저 uid(로그인, 비로그인 포함)
     * @return 해당 유저 장바구니 존재하면 1, 아니면 0 반환
     * @throws Exception .
     */
    @Override
    public int checkUserCartExist(String userId) throws Exception {
        return cartDao.countUserCart(userId);
    }

    /**
     * 해당 유저 장바구니 상품 개수 조회
     *
     * @param userId 유저 uid(로그인, 비로그인 포함)
     * @return 해당 유저가 장바구니에 담은 상품 수 반환
     * @throws Exception .
     */
    @Override
    public int getUserCartProductsCount(String userId) throws Exception {
        return cartDao.countUserCartProducts(userId);
    }

    /**
     * 해당 유저 장바구니 생성
     *
     * @param userId 유저 uid(로그인, 비로그인 포함)
     * @param isUser 로그인 비로그인 확인
     * @return 장바구니 생성 성공 시 1, 실패 시 0 반환
     * @throws Exception .
     */
    @Override
    public int createUserCart(String userId, int isUser) throws Exception {
        return cartDao.createCart(userId, isUser);
    }

    /**
     * 해당 유저 장바구니 상품 정보 조회
     *
     * @param userId 유저 uid(로그인, 비로그인 포함)
     * @param isUser 로그인 비로그인 확인
     * @return 상품 상세 정보
     * @throws Exception .
     */
    @Override
    public List<CartDto> getUserCartProducts(String userId, int isUser) throws Exception {
        return cartDao.selectUserCartProductsByUserId(userId);
    }

    /**
     * 해당 유저 장바구니에 상품 추가
     *
     * @param userId    유저 uid(로그인, 비로그인 포함)
     * @param productId 상품 id
     * @param quantity  상품 수량
     * @param isUser    로그인 비로그인 확인
     * @throws Exception .
     */
    @Override
    @Transactional
    public void addProductToCart(String userId, Long productId, Long quantity, int isUser) throws Exception {
        ensureUserCartExists(userId, isUser);

        ProductDto isExistProduct = productDao.select(productId.intValue());
        if (isExistProduct == null) {
            throw new CartNotFoundException("상품 번호" + productId + "를 찾을 수 없음");
        }

        // 해당 유저의 장바구니에서 상품을 가져옴
        CartDto specificProduct = cartDao.searchProductIdByUserIdAndProductId(userId, productId);

        // 이미 장바구니에 존재하는 경우 수량을 더함
        long totalQuantity = (specificProduct != null) ? specificProduct.getQuantity() + quantity : quantity;

        // 수량 제한 적용: 최대 100, 최소 1
        totalQuantity = Math.max(1, Math.min(totalQuantity, 100));

        if (specificProduct != null) {
            // 기존 제품이 있을 경우 수량 업데이트
            updateProductQuantity(userId, productId, totalQuantity);
        } else {
            // 새로 추가하는 경우
            int result = cartDao.insertUserCartProduct(userId, productId, totalQuantity);
            if (result > 0) {
                updateCartLastUpdate(userId);
            }
        }
    }


    /**
     * 해당 유저 장바구니 상품 수량 수정
     *
     * @param userId    유저 uid(로그인, 비로그인 포함)
     * @param productId 상품 id
     * @param quantity  상품 수량
     * @throws Exception .
     */
    @Override
    @Transactional
    public int updateProductQuantity(String userId, Long productId, Long quantity) throws Exception {
        ProductDto isExistProduct = productDao.select(productId.intValue());
        if (isExistProduct == null) {
            throw new CartNotFoundException("상품 번호" + productId + "를 찾을 수 없음");
        }

        quantity = Math.max(1, Math.min(quantity, 100));

        int result = cartDao.updateUserCartProductQuantity(userId, productId, quantity);
        if (result > 0) {
            updateCartLastUpdate(userId);
        }

        return result;
    }

    /**
     * 장바구니 페이지에서 선택한 상품들 삭제하는 메서드
     *
     * @param userId     유저 uid(로그인, 비로그인 포함)
     * @param productIds 상품 id가 포함된 리스트 [1,2,3]
     * @throws Exception .
     */
    @Override
    @Transactional
    public int deleteCartProducts(String userId, List<Long> productIds) throws Exception {
        for (Long productId : productIds) {
            deleteSpecificProduct(userId, productId);
        }
        return updateCartLastUpdate(userId);
    }

    /**
     * 장바구니 특정 상품 삭제하는 메서드
     *
     * @param userId    유저 uid(로그인, 비로그인 포함)
     * @param productId 상품 id
     * @throws Exception .
     */
    @Override
    @Transactional
    public void deleteSpecificProduct(String userId, Long productId) throws Exception {
        CartDto specificProduct = cartDao.searchProductIdByUserIdAndProductId(userId, productId);
        if (specificProduct == null) {
            return;
        }
        int result = cartDao.deleteUserCartProduct(userId, productId);
        if (result > 0) {
            updateCartLastUpdate(userId);
        }
    }

    /**
     * 로그인하면 비로그인에서 담은 장바구니를 유저 장바구니로 담는 메서드
     *
     * @param loginUser 로그인 유저 uid
     * @param guestId   게스트 id
     * @param isUser    로그인 비로그인 확인
     * @throws Exception .
     */
    @Override
    @Transactional
    public void updateGuestCartToUser(String loginUser, String guestId, int isUser) throws Exception {
        if (!isCartTransferNeeded(guestId)) {
            return;
        }

        transferGuestCartToUser(loginUser, guestId, isUser);
        if (removeGuestCart(guestId) == 0) {
            throw new CartNotFoundException("해당 유저의 장바구니를 찾을 수 없음");
        }
    }

    private boolean isCartTransferNeeded(String guestId) throws Exception {
        return isGuestCartNotEmpty(guestId) && getUserCartProductsCount(guestId) > 0;
    }

    private boolean isGuestCartNotEmpty(String guestId) throws Exception {
        return checkUserCartExist(guestId) == 1;
    }

    private void transferGuestCartToUser(String loginUser, String guestId, int isUser) throws Exception {
        List<CartDto> guestCartProducts = getGuestCartProducts(guestId);
        for (CartDto cartDto : guestCartProducts) {
            addProductToCart(loginUser, cartDto.getProductId(), cartDto.getQuantity(), isUser);
        }
    }

    private List<CartDto> getGuestCartProducts(String guestId) throws Exception {
        return cartDao.selectUserCartProductsByUserId(guestId);
    }

    private int removeGuestCart(String guestId) throws Exception {
        return cartDao.deleteUserCart(guestId);
    }


    /**
     * 장바구니 수정일(lastUpdate) 업데이트 메서드
     *
     * @param userId 유저 uid(로그인, 비로그인 포함)
     */
    public int updateCartLastUpdate(String userId) throws Exception {
        return cartDao.updateCartLastUpdate(userId);
    }

    /**
     * 장바구니가 없는 경우 생성하는 메서드
     *
     * @param userId 유유저 uid(로그인, 비로그인 포함)
     * @param isUser 로그인 비로그인 확인
     */
    private void ensureUserCartExists(String userId, Integer isUser) throws Exception {
        if (checkUserCartExist(userId) != 1) {
            createUserCart(userId, isUser);
        }
    }
}
