package com.toy2.shop29.cart.controller;

import com.toy2.shop29.cart.domain.request.DeleteCartItemsRequestDto;
import com.toy2.shop29.cart.domain.response.CartDto;
import com.toy2.shop29.cart.service.CartItemService;
import com.toy2.shop29.cart.service.CartManagementService;
import com.toy2.shop29.common.ProductItem;
import com.toy2.shop29.users.domain.UserContext;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    // 예외 발생 시 추적할 수 있게 로깅 추가
    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    private final CartManagementService cartManagementService;
    private final CartItemService cartItemService;

    /**
     * 장바구니 페이지 요청
     *
     * @param user    유저 uid(로그인, 비로그인 포함)
     * @param guestId 게스트 id
     * @return 장바구니 페이지
     * @throws Exception .
     */
    @GetMapping("")
    public ModelAndView getCartList(
            @AuthenticationPrincipal UserContext user,
            @CookieValue(name = "guestId", required = false) String guestId,
            Model model) {
        ModelAndView mav = new ModelAndView("cart/cart");
        // 로그인과 비로그인 사용자 구분
        String userInfo = getUserInfo(user, guestId);
        int isUser = (user != null) ? 1 : 0;

        // user 고유 아이디와 로그인 유무로 장바구니 조회
        // 장바구니가 존재하지 않다면 생성까지
        // 장바구니 조회 성공 시 모델 객체에 장바구니 추가
        try {
            List<CartDto> getAllCart = cartManagementService.getUserCartProducts(userInfo, isUser);
            mav.addObject("cartList", getAllCart);
            mav.addObject("isLogin", isUser);
        } catch (Exception e) {
            logger.error("장바구니 리스트 조회 실패", e);
        }

        // 뷰 반환
        return mav;
    }

    /**
     * 장바구니 담기
     *
     * @param user    유저 uid(로그인, 비로그인 포함)
     * @param guestId 게스트 id
     * @return 장바구니 페이지
     * @throws Exception .
     */
    @PostMapping("/cart-item")
    public ResponseEntity<Map<String, String>> addCartItem(
            @AuthenticationPrincipal UserContext user,
            @CookieValue(name = "guestId", required = false) String guestId,
            @Valid @RequestBody ProductItem addCartProductDto) {

        // 로그인과 비로그인 사용자 구분
        String userInfo = getUserInfo(user, guestId);
        int isUser = (user != null) ? 1 : 0;

        Map<String, String> response = new HashMap<>();

        // 유저 고유 아이디와 상품 아이디, 수량으로 장바구니에 담기
        // 장바구니에 이미 존재하는 상품이라면 담은 수량만큼 추가
        try {
            // 장바구니에 상품 추가
            cartItemService.addProductToCart(userInfo, addCartProductDto, isUser);
            response.put("status", "success");
            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (Exception e) {
            logger.error("장바구니 상품 추가 실패", e);
            response.put("status", "fail");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 장바구니 상품 수량 변경
     *
     * @param productItem 상품 id, 상품 수량
     * @param userId      로그인 유저 uid
     * @param guestId     비로그인 유저 id
     * @return 성공 실패
     * @throws Exception .
     */
    @PostMapping("/order-count")
    public ResponseEntity<Map<String, String>> orderCount(
            @RequestBody ProductItem productItem,
            @SessionAttribute(name = "loginUser", required = false) String userId,
            @AuthenticationPrincipal UserContext user,
            @CookieValue(name = "guestId", required = false) String guestId) {
        String userInfo = getUserInfo(user, guestId);
        Map<String, String> response = new HashMap<>();
        try {
            cartItemService.updateProductQuantity(userInfo, productItem);
            response.put("status", "success");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            logger.error("상품 수량 수정 오류", e);
            response.put("status", "fail");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    /**
     * 장바구니 상품 삭제
     *
     * @param deleteRequest 상품 id 리스트
     * @param user          로그인 유저 uid
     * @param guestId       비로그인 유저 id
     * @return 성공 실패
     * @throws Exception .
     */

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteCartItems(
            @RequestBody List<DeleteCartItemsRequestDto> deleteRequest,
            @AuthenticationPrincipal UserContext user,
            @CookieValue(name = "guestId", required = false) String guestId
    ) {

        // 로그인과 비로그인 사용자 구분
        String userInfo = getUserInfo(user, guestId);

        try {
            cartItemService.deleteCartProducts(userInfo, deleteRequest);
            return ResponseEntity.ok("삭제 완료");
        } catch (Exception e) {
            logger.error("상품 삭제 중 오류", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("상품 삭제 중 오류");
        }
    }

    // 로그인 비로그인 검증
    public String getUserInfo(UserContext userContext, String guestId) {
        return (userContext != null) ? userContext.getUserDto().getUserId() : guestId;
    }
}