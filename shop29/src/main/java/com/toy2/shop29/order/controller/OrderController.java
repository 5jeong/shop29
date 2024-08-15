package com.toy2.shop29.order.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.toy2.shop29.cart.controller.CartController;
import com.toy2.shop29.order.domain.pay.ReadyResponseDto;
import com.toy2.shop29.order.domain.request.OrderCompletedRequestDTO;
import com.toy2.shop29.order.domain.request.OrderProductDto;
import com.toy2.shop29.order.domain.response.OrderHistoryDTO;
import com.toy2.shop29.order.domain.response.OrderPageResponseDTO;
import com.toy2.shop29.order.service.KakaoPayService;
import com.toy2.shop29.order.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.HttpSessionRequiredException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    // 예외 발생 시 추적할 수 있게 로깅 추가
    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private KakaoPayService kakaoPayService;

    /**
     * 주문 페이지
     *
     * @param userId 유저 uid(로그인, 비로그인 포함)
     * @return 주문 페이지
     * @throws Exception .
     */
    @GetMapping("")
    public String order(
            @SessionAttribute(name = "loginUser", required = true) String userId,
            Model model,
            HttpServletResponse response) throws Exception {
        // 응답에 대한 모든 데이터를 디스크, 메모리에 저장하지 않음. 모든 요청이 서버로 직접 전달
        response.setHeader("Cache-Control", "no-store");
        // 응답을 캐시하지 않도록 함
        response.setHeader("Pragma", "no-cache");
        // 응답 즉시 만료 설정
        response.setDateHeader("Expires", 0);

        // TODO : 로그인 확인
        // 주문에 필요한 데이터 모델에 추가하여 뷰로 전달
        OrderPageResponseDTO orderInfo = orderService.getCurrentOrderInfo(userId);

        // 주문 상품이 비어져있다는건 오류나 이미 결제 시도한 상태
        // 주문 상품이 비어있다면 장바구니로 리다이렉트
        if (orderInfo.getCurrentItemsData().isEmpty()) {
            return "redirect:/cart";
        }

        model.addAttribute("order", orderInfo);
        return "order/order";
    }

    /**
     * 주문 내역 페이지
     *
     * @param userId 유저 uid(로그인, 비로그인 포함)
     * @return 주문 내역 페이지
     * @throws Exception .
     */
    @GetMapping("/history")
    public String orderHistory(
            @SessionAttribute(name = "loginUser", required = true) String userId,
            Model model) throws Exception {

        List<OrderHistoryDTO> orderHistoryList = orderService.getOrderHistory(userId);

        // 주문 내역 리스트를 모델에 추가하여 뷰에 전달
        model.addAttribute("orderHistory", orderHistoryList);

        return "order/orderList";
    }

    /**
     * 주문 할 상품들 주문 처리 테이블에 추가
     *
     * @param userId         유저 uid(로그인, 비로그인 포함)
     * @param orderItemsJson 상품 id, 수량 리스트
     * @return 성공 시 주문 페이지로 리다이렉트
     * @throws Exception .
     */
    @PostMapping("/update-order")
    public String updateOrderProducts(
            @SessionAttribute(name = "loginUser", required = true) String userId,
            @RequestParam("orderItems") String orderItemsJson,
            RedirectAttributes rattr) {

        ObjectMapper objectMapper = new ObjectMapper();

        // TODO : 로그인 확인
        if (!logInCheck(userId)) {
            return "redirect:/login";
        }
        try {
            List<OrderProductDto> orderItems = objectMapper.readValue(orderItemsJson, new TypeReference<>() {
            });
            orderService.addProducts(userId, orderItems);
        } catch (IllegalArgumentException e) {
            logger.error("상품 수량 수정 오류");
            rattr.addFlashAttribute("msg", "ORDER_ERR");
            return "redirect:/cart";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "redirect:/order";
    }

    /**
     * 카카오페이 결제 페이지에 필요한 데이터
     *
     * @param userId       유저 uid(로그인, 비로그인 포함)
     * @param orderRequest 상품 리스트, 총 가격, 유저 배송지
     * @return 결제 고유번호와 카카오페이 결제 리다이렉트 URL
     * @throws Exception .
     */
    @PostMapping("/pay/ready")
    public @ResponseBody ReadyResponseDto payReady(
            @SessionAttribute(name = "loginUser", required = true) String userId,
            Model model,
            @RequestBody OrderCompletedRequestDTO orderRequest,
            HttpServletRequest request) {
        // TODO : 로그인 확인
        // 카카오 결제 준비하기
        try {
            ReadyResponseDto ReadyResponseDto = kakaoPayService.payReady(userId, orderRequest);
            // 세션에 결제 고유번호(tid) 저장
            HttpSession session = request.getSession(true);
            session.setAttribute("tid", ReadyResponseDto.getTid());
            logger.info("결제 고유번호: " + ReadyResponseDto.getTid());
            logger.info("결제 리다이렉트: " + ReadyResponseDto.getNext_redirect_pc_url());
            return ReadyResponseDto;
        } catch (Exception e) {
            // TODO : 예외처리 추가
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * 카카오페이 완료 페이지
     *
     * @param userId  유저 uid(로그인, 비로그인 포함)
     * @param pgToken 결제승인 요청 인증 토큰
     * @return 결제 성공, 실패, 취소, 오류에 대한 페이지로 리다이렉트
     * @throws Exception .
     */
    @GetMapping("/pay/completed")
    public String payCompleted(
            @SessionAttribute(name = "loginUser", required = true) String userId,
            @RequestParam("pg_token") String pgToken,
            HttpServletRequest request) {
        // TODO : 로그인 확인
        HttpSession session = request.getSession(true);
        String tid = (String) session.getAttribute("tid");
        logger.info("결제승인 요청을 인증하는 토큰: " + pgToken);
        logger.info("결제 고유번호: " + tid);

        // 카카오 결제 요청하기
        try {
            kakaoPayService.payApprove(userId, tid, pgToken);
            // TODO : 테스트
        } catch (Exception e) {
            return "redirect:/order/pay/error";
        }

        session.removeAttribute("tid");
        return "redirect:/order/history";
    }

    /**
     * 카카오페이 취소 페이지
     *
     * @return 결제 취소 페이지
     * @throws Exception .
     */
    @GetMapping("/pay/cancel")
    public String payCancel() throws Exception {
        // TODO : 로그인 확인
        return "pay/payCancel";
    }

    /**
     * 카카오페이 실패 페이지
     *
     * @return 결제 실패 페이지
     * @throws Exception .
     */
    @GetMapping("/pay/fail")
    public String payFail() throws Exception {
        // TODO : 로그인 확인
        // TODO : 결제 실패 시 장바구니로 상품 복구
        return "pay/payFail";
    }

    /**
     * 카카오페이 오류 페이지
     *
     * @return 결제 오류 페이지
     * @throws Exception .
     */
    @GetMapping("/pay/error")
    public String payError() throws Exception {
        return "pay/payError";
    }

    private boolean logInCheck(String userId) {
        if (userId == null)
            return false;
        return true;
    }

    @ExceptionHandler(HttpSessionRequiredException.class)
    public String handleSessionException(HttpSessionRequiredException ex) {
        return "redirect:/login";
    }
}
