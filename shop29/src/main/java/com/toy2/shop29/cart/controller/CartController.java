package com.toy2.shop29.cart.controller;

import com.toy2.shop29.cart.domain.response.CartDto;
import com.toy2.shop29.cart.domain.request.AddCartProductDto;
import com.toy2.shop29.cart.domain.request.DeleteCartItemsRequestDto;
import com.toy2.shop29.cart.domain.request.OrderCountRequestDto;
import com.toy2.shop29.cart.service.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/cart")
public class CartController {

    // 예외 발생 시 추적할 수 있게 로깅 추가
    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    @Autowired
    private CartService cartService;

    // 장바구니 페이지 요청
    @GetMapping("/get-list")
    public String getCartList(@SessionAttribute(name = "loginUser", required = false) String userId,
                              @CookieValue(name = "guestId", required = false) String guestId,
                              Model model) {

        // 로그인과 비로그인인지 확인해서 장바구니에 담는 것
        // TODO : 아래 코드를 어떻게 하면 깔끔하게 작성할 수 있을지 고민
        String userInfo = getUserInfo(userId, guestId);
        int isUser = (userId != null) ? 1 : 0;

        // user 고유 아이디와 로그인 유무로 장바구니 조회
        // 장바구니가 존재하지 않다면 생성까지
        // 장바구니 조회 성공 시 모델 객체에 장바구니 추가
        try {
            List<CartDto> getAllCart = cartService.getUserCartProducts(userInfo, isUser);
            model.addAttribute("cartList", getAllCart);
        } catch (Exception e) {
            logger.error("장바구니 리스트 조회 실패", e);
        }

        // 뷰 반환
        return "cart/cart";
    }

    // 장바구니 담기
    @PostMapping("/cart-item")
    public ResponseEntity<Map<String, String>> addCartItem(@SessionAttribute(name = "loginUser", required = false) String userId,
                                                           @CookieValue(name = "guestId", required = false) String guestId,
                                                           @RequestBody AddCartProductDto addCartProductDto) {

        // 로그인과 비로그인인지 확인해서 장바구니에 담는 것
        // TODO : 아래 코드를 어떻게 하면 깔끔하게 작성할 수 있을지 고민
        String userInfo = getUserInfo(userId, guestId);
        int isUser = (userId != null) ? 1 : 0;

        Map<String, String> response = new HashMap<>();

        // 유저 고유 아이디와 상품 아이디, 수량으로 장바구니에 담기
        // 장바구니에 이미 존재하는 상품이라면 담은 수량만큼 추가
        try {
            Long productId = addCartProductDto.getProductId();
            Long quantity = addCartProductDto.getQuantity();
            if (quantity < 1){
                // TODO : 상품 수량 1로
                // TODO : RestController
                response.put("status", "fail");
                response.put("message", "상품 수량 문제");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            cartService.addProductToCart(userInfo, productId, quantity, isUser);
            response.put("status", "success");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            logger.error("장바구니 상품 추가 실패", e);
            response.put("status", "fail");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 장바구니 상품 수량 변경
    @PostMapping("/order-count")
    public ResponseEntity<Map<String, String>> orderCount(@RequestBody OrderCountRequestDto orderCountRequestDto,
                                                          @SessionAttribute(name = "loginUser", required = false) String userId,
                                                          @CookieValue(name = "guestId", required = false) String guestId) {

        String userInfo = getUserInfo(userId, guestId);
        Map<String, String> response = new HashMap<>();

        //
        try {
            cartService.updateProductQuantity(userInfo, orderCountRequestDto.getProduct_id(), orderCountRequestDto.getQuantity());
            response.put("status", "success");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            logger.error("상품 수량 수정 에러", e);
            response.put("status", "fail");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 장바구니 상품 삭제
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteCartItems(@SessionAttribute(name = "loginUser", required = false) String userId,
                                                  @CookieValue(name = "guestId", required = false) String guestId,
                                                  @RequestBody DeleteCartItemsRequestDto deleteRequest) {

        String userInfo = getUserInfo(userId, guestId);

        try {
            cartService.deleteCartProducts(userInfo, deleteRequest.getProductIds());
            return ResponseEntity.ok("삭제 완료");
        } catch (Exception e) {
            logger.error("상품 삭제 중 오류", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("상품 삭제 중 오류");
        }
    }

    // 로그인 비로그인 검증
    public String getUserInfo(String userId, String guestId) {
        return (userId != null) ? userId : guestId;
    }
}
