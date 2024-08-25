package com.toy2.shop29.cart.service;

import com.toy2.shop29.cart.dao.CartDao;
import com.toy2.shop29.cart.domain.request.DeleteCartItemsRequestDto;
import com.toy2.shop29.cart.domain.response.CartDto;
import com.toy2.shop29.cart.exception.CartNotFoundException;
import com.toy2.shop29.cart.exception.ProductNotFoundException;
import com.toy2.shop29.common.ProductItem;
import com.toy2.shop29.product.dao.product.ProductDao;
import com.toy2.shop29.product.domain.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartDao cartDao;
    private final ProductDao productDao;

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
     * 사용자의 장바구니에 담긴 상품의 개수를 조회하는 메서드
     *
     * @param userId 사용자의 UID (로그인 여부와 무관)
     * @return 장바구니에 담긴 상품의 수량
     * @throws Exception 조회 중 오류가 발생할 경우
     */

    @Override
    public int getUserCartProductsCount(String userId) throws Exception {
        return cartDao.countUserCartProducts(userId);
    }

    /**
     * 사용자의 장바구니를 생성하는 메서드
     *
     * @param userId 사용자의 UID (로그인 여부와 무관)
     * @param isUser 로그인 여부를 나타내는 플래그 (1이면 로그인, 0이면 비로그인)
     * @return 장바구니 생성 성공 시 1, 실패 시 0 반환
     * @throws Exception 생성 중 오류가 발생할 경우
     */
    @Override
    public int createUserCart(String userId, int isUser) throws Exception {
        return cartDao.createCart(userId, isUser);
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
     * 사용자의 장바구니에 상품을 추가하는 메서드
     *
     * @param userId            사용자의 UID (로그인 여부와 무관)
     * @param addCartProductDto 장바구니에 추가할 상품 정보
     * @param isUser            로그인 여부를 나타내는 플래그 (1이면 로그인, 0이면 비로그인)
     * @throws Exception 상품 추가 중 오류가 발생할 경우
     */
    @Override
    @Transactional
    public void addProductToCart(String userId, ProductItem addCartProductDto, int isUser) throws Exception {
        Long productId = addCartProductDto.getProductId();
        Long quantity = applyQuantityLimits(addCartProductDto.getQuantity());

        ensureUserCartExists(userId, isUser);
        validateProductExists(productId);

        CartDto specificProduct = cartDao.searchProductIdByUserIdAndProductId(userId, productId, addCartProductDto.getProductOptionId());

        long totalQuantity = calculateTotalQuantity(specificProduct, quantity);
        ProductItem updatedProductItem = new ProductItem(productId, totalQuantity, addCartProductDto.getProductOptionId());

        if (specificProduct != null) {
            updateProductQuantity(userId, updatedProductItem);
        } else {
            addNewProductToCart(userId, updatedProductItem);
        }

        updateCartLastUpdate(userId);
    }

    /**
     * 사용자의 장바구니에 담긴 특정 상품의 수량을 수정하는 메서드
     *
     * @param userId      사용자의 UID (로그인 여부와 무관)
     * @param productItem 수정할 상품 정보
     * @return 수량 수정 성공 시 1, 실패 시 0 반환
     * @throws Exception 수량 수정 중 오류가 발생할 경우
     */
    @Override
    @Transactional
    public int updateProductQuantity(String userId, ProductItem productItem) throws Exception {
        validateProductExists(productItem.getProductId());

        Long quantity = applyQuantityLimits(productItem.getQuantity());

        int result = updateUserCartProductQuantity(userId, productItem.getProductId(), quantity, productItem.getProductOptionId());
        if (result > 0) {
            updateCartLastUpdate(userId);
        }

        return result;
    }

    /**
     * 사용자의 장바구니에서 선택한 상품들을 삭제하는 메서드
     *
     * @param userId     사용자의 UID (로그인 여부와 무관)
     * @param productIds 삭제할 상품 ID와 옵션 ID를 포함한 리스트
     * @return 삭제 작업 후 장바구니 업데이트 성공 시 1, 실패 시 0 반환
     * @throws Exception 삭제 중 오류가 발생할 경우
     */
    @Override
    @Transactional
    public int deleteCartProducts(String userId, List<DeleteCartItemsRequestDto> productIds) throws Exception {
        for (DeleteCartItemsRequestDto product : productIds) {
            deleteSpecificProduct(userId, product.getProductId(), product.getProductOptionId());
        }
        return updateCartLastUpdate(userId);
    }

    /**
     * 사용자의 장바구니에서 특정 상품을 삭제하는 메서드
     *
     * @param userId          사용자의 UID (로그인 여부와 무관)
     * @param productId       삭제할 상품의 ID
     * @param productOptionId 삭제할 상품의 옵션 ID
     * @throws Exception 삭제 중 오류가 발생할 경우
     */
    @Override
    @Transactional
    public void deleteSpecificProduct(String userId, Long productId, Long productOptionId) throws Exception {
        CartDto specificProduct = cartDao.searchProductIdByUserIdAndProductId(userId, productId, productOptionId);
        if (specificProduct == null) {
            return;
        }
        int result = cartDao.deleteUserCartProduct(userId, productId, productOptionId);
        if (result > 0) {
            updateCartLastUpdate(userId);
        }
    }

    /**
     * 비로그인 사용자가 담은 장바구니를 로그인 사용자에게 이전하는 메서드
     *
     * @param loginUser 로그인한 사용자의 UID
     * @param guestId   비로그인 사용자의 UID
     * @param isUser    로그인 여부를 나타내는 플래그 (1이면 로그인, 0이면 비로그인)
     * @throws Exception 장바구니 이전 중 오류가 발생할 경우
     */
    @Override
    @Transactional
    public void updateGuestCartToUser(String loginUser, String guestId, int isUser) throws Exception {
        if (!isCartTransferNeeded(guestId)) {
            return;
        }

        List<ProductItem> guestCartProducts = selectUserCartItem(guestId);
        for (ProductItem cartDto : guestCartProducts) {
            addProductToCart(loginUser, cartDto, isUser);
        }
        if (removeGuestCart(guestId) == 0) {
            throw new CartNotFoundException("해당 유저의 장바구니를 찾을 수 없음");
        }
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

    /**
     * 사용자의 장바구니가 존재하지 않는 경우, 장바구니를 생성하는 메서드
     *
     * @param userId 사용자의 UID (로그인 여부와 무관)
     * @param isUser 로그인 여부를 나타내는 플래그 (1이면 로그인, 0이면 비로그인)
     * @throws Exception 장바구니 생성 중 오류가 발생할 경우
     */
    private void ensureUserCartExists(String userId, Integer isUser) throws Exception {
        if (checkUserCartExist(userId) != 1) {
            createUserCart(userId, isUser);
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
        return isGuestCartNotEmpty(guestId) && getUserCartProductsCount(guestId) > 0;
    }

    /**
     * 비로그인 사용자의 장바구니가 비어 있지 않은지 확인하는 메서드
     *
     * @param guestId 비로그인 사용자의 UID
     * @return 장바구니가 비어 있지 않으면 true, 비어 있으면 false 반환
     * @throws Exception 확인 중 오류가 발생할 경우
     */
    private boolean isGuestCartNotEmpty(String guestId) throws Exception {
        return checkUserCartExist(guestId) == 1;
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

    /**
     * 기존 상품 수량과 새로 추가된 수량을 계산하여 총 수량을 반환하는 메서드
     *
     * @param specificProduct 장바구니에 이미 존재하는 특정 상품
     * @param quantity        새로 추가된 수량
     * @return 계산된 총 수량 반환
     */
    private long calculateTotalQuantity(CartDto specificProduct, Long quantity) {
        if (specificProduct != null) {
            return Math.max(1, Math.min(specificProduct.getQuantity() + quantity, 100));
        }
        return quantity;
    }

    /**
     * 장바구니에 새 상품을 추가하는 메서드
     *
     * @param userId             유저 uid(로그인, 비로그인 포함)
     * @param updatedProductItem 추가할 상품 정보
     * @throws Exception 상품 추가 실패 시 예외 발생
     */
    private void addNewProductToCart(String userId, ProductItem updatedProductItem) throws Exception {
        int result = cartDao.insertUserCartProduct(userId, updatedProductItem.getProductId(),
                updatedProductItem.getQuantity(),
                updatedProductItem.getProductOptionId());
        if (result <= 0) {
            throw new Exception("Failed to add new product to cart");
        }
    }

    /**
     * 주어진 상품 ID에 해당하는 상품이 존재하는지 확인하는 메서드
     *
     * @param productId 상품 ID
     * @throws ProductNotFoundException 상품이 존재하지 않을 경우 예외 발생
     */
    private void validateProductExists(Long productId) throws ProductNotFoundException {
        ProductDto isExistProduct = productDao.select(productId.intValue());
        if (isExistProduct == null) {
            throw new ProductNotFoundException("상품 번호 " + productId + "번을 찾을 수 없음");
        }
    }

    /**
     * 상품 수량 제한을 적용하는 메서드 (최소 1, 최대 100)
     *
     * @param quantity 적용할 수량
     * @return 제한이 적용된 수량 반환
     */
    private Long applyQuantityLimits(Long quantity) {
        return Math.max(1, Math.min(quantity, 100));
    }

    /**
     * 장바구니에 있는 특정 상품의 수량을 업데이트하는 메서드
     *
     * @param userId 사용자의 UID (로그인 여부와 무관)
     * @param productId 상품 ID
     * @param quantity 업데이트할 수량
     * @param productOptionId 상품 옵션 ID
     * @return 업데이트 성공 시 1, 실패 시 0 반환
     * @throws Exception 업데이트 중 오류가 발생할 경우
     */
    private int updateUserCartProductQuantity(String userId, Long productId, Long quantity, Long productOptionId) throws Exception {
        return cartDao.updateUserCartProductQuantity(userId, productId, quantity, productOptionId);
    }
}