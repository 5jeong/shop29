package com.toy2.shop29.order.controller;

import com.toy2.shop29.cart.controller.CartController;
import com.toy2.shop29.order.domain.pay.ReadyResponse;
import com.toy2.shop29.order.domain.request.AddCurrentOrderRequest;
import com.toy2.shop29.order.domain.request.OrderCompletedRequestDTO;
import com.toy2.shop29.order.domain.request.OrderProductDto;
import com.toy2.shop29.order.domain.response.OrderHistoryDTO;
import com.toy2.shop29.order.domain.response.OrderPageResponseDTO;
import com.toy2.shop29.order.service.KakaoPayService;
import com.toy2.shop29.order.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/order")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private KakaoPayService kakaoPayService;

    @GetMapping("")
    public String order(@SessionAttribute(name = "loginUser", required = true) String userId, Model model,
                        @SessionAttribute(name = "tid", required = false) String tid) throws Exception {
        // TODO : 로그인 확인
        // 주문에 필요한 데이터 모델에 추가하여 뷰로 전달
        OrderPageResponseDTO orderInfo = orderService.getCurrentOrderInfo(userId);
        if (orderInfo.getCurrentItemsData().isEmpty()) {
            return "redirect:/cart/get-list";
        }
        model.addAttribute("order", orderInfo);
        return "order/order";
    }

    @GetMapping("/history")
    public String orderHistory(@SessionAttribute(name = "loginUser", required = true) String userId, Model model) throws Exception {
        List<OrderHistoryDTO> orderHistoryList = orderService.getOrderHistory(userId);

        // 주문 내역 리스트를 모델에 추가하여 뷰에 전달합니다.
        model.addAttribute("orderHistory", orderHistoryList);

        return "order/orderList";
    }

    @PostMapping("/update-order")
    public ResponseEntity<Map<String, String>> updateOrderProducts(@SessionAttribute(name = "loginUser", required = true) String userId,
                                                                   @RequestBody AddCurrentOrderRequest addCurrentOrderProductDto) {
        Map<String, String> response = new HashMap<>();
        // TODO : 로그인 확인
        if (!logInCheck(userId)) {
            response.put("redirectUrl", "/login");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        List<OrderProductDto> productDtoList = addCurrentOrderProductDto.getOrderItems();
        try {
            orderService.addProducts(userId, productDtoList);
            response.put("status", "success");
            response.put("redirectUrl", "/order");
        } catch (Exception e) {
            logger.error("상품 수량 수정 에러", e);
            response.put("status", "fail");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/pay/ready")
    public @ResponseBody ReadyResponse payReady(@SessionAttribute(name = "loginUser", required = true) String userId, Model model,
                                                @RequestBody OrderCompletedRequestDTO orderRequest,
                                                HttpServletRequest request) {
        // TODO : 로그인 확인
        // 카카오 결제 준비하기
        try {
            ReadyResponse readyResponse = kakaoPayService.payReady(userId, orderRequest);
            // 세션에 결제 고유번호(tid) 저장
            HttpSession session = request.getSession(true);
            session.setAttribute("tid", readyResponse.getTid());
            logger.info("결제 고유번호: " + readyResponse.getTid());
            return readyResponse;
        } catch (Exception e) {
            // TODO : 예외처리 추가
            throw new IllegalArgumentException(e);
        }
    }

    @GetMapping("/pay/completed")
    public String payCompleted(@SessionAttribute(name = "loginUser", required = true) String userId,
                               @RequestParam("pg_token") String pgToken, RedirectAttributes redirectAttributes,
                               HttpServletRequest request) {
        // TODO : 로그인 확인
        HttpSession session = request.getSession(true);
        String tid = (String) session.getAttribute("tid");
        logger.info("결제승인 요청을 인증하는 토큰: " + pgToken);
        logger.info("결제 고유번호: " + tid);

        // 카카오 결제 요청하기
        try {
            kakaoPayService.payApprove(userId, tid, pgToken);
        } catch (Exception e) {
            return "redirect:/order/pay/error";
        }
        session.removeAttribute("tid");
        return "redirect:/order/history";
    }

    @GetMapping("/pay/cancel")
    public String payCancel(@SessionAttribute(name = "loginUser", required = true) String userId, HttpServletRequest request) throws Exception {
        // TODO : 로그인 확인

        HttpSession session = request.getSession(true);
        String tid = (String) session.getAttribute("tid");
        session.removeAttribute("tid");

        orderService.deleteOrderHistory(userId, tid);
        return "pay/payCancel";
    }

    @GetMapping("/pay/fail")
    public String payFail(@SessionAttribute(name = "loginUser", required = true) String userId, HttpServletRequest request) throws Exception {
        // TODO : 로그인 확인
        HttpSession session = request.getSession(true);
        String tid = (String) session.getAttribute("tid");
        orderService.deleteOrderHistory(userId, tid);
        session.removeAttribute("tid");
        return "pay/payFail";
    }

    private boolean logInCheck(String userId) {
        if (userId == null)
            return false;
        return true;
    }
}
