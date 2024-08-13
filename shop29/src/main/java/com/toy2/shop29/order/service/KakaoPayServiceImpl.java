package com.toy2.shop29.order.service;

import com.toy2.shop29.cart.controller.CartController;
import com.toy2.shop29.order.domain.pay.ReadyResponse;
import com.toy2.shop29.order.domain.request.OrderProductDto;
import com.toy2.shop29.order.domain.request.OrderCompletedRequestDTO;
import com.toy2.shop29.order.utils.GenerateId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class KakaoPayServiceImpl implements KakaoPayService {
    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    @Value("${security.secret-key}")
    private String secretKey;

    @Autowired
    private OrderService orderService;

    // 카카오페이 결제창 연결
    public ReadyResponse payReady(String userId, OrderCompletedRequestDTO orderRequest) throws Exception {
        String orderId = GenerateId.generateId();
        List<OrderProductDto> products = orderRequest.getOrderItems();

        Long totalQuantity = products.stream()
                .mapToLong(OrderProductDto::getQuantity)
                .sum();

        long totalPrice = orderRequest.getTotalPrice();

        String productName = orderService.selectProductNameByProductId(products.get(0).getProductId());

        if (products.size() > 1) {
            int additionalProductsCount = products.size() - 1;
            productName += " 외 " + additionalProductsCount + "개";
        }

        logger.info("수량: " + totalQuantity);
        logger.info("금액: " + totalPrice);
        logger.info("상품명: " + productName);


        Map<String, String> parameters = new HashMap<>();
        parameters.put("cid", "TC0ONETIME");                                    // 가맹점 코드(테스트용)
        parameters.put("partner_order_id", "1234567890");                       // 주문번호
        parameters.put("partner_user_id", "roommake");                          // 회원 아이디
        parameters.put("item_name", productName);                                      // 상품명
        parameters.put("quantity", String.valueOf(totalQuantity));              // 상품 수량
        parameters.put("total_amount", String.valueOf(totalPrice));             // 상품 총액
        parameters.put("tax_free_amount", "0");                                 // 상품 비과세 금액
        parameters.put("approval_url", "http://localhost:8080/order/pay/completed"); // 결제 성공 시 URL
        parameters.put("cancel_url", "http://localhost:8080/order/pay/cancel");      // 결제 취소 시 URL
        parameters.put("fail_url", "http://localhost:8080/order/pay/fail");          // 결제 실패 시 URL

        // HttpEntity : HTTP 요청 또는 응답에 해당하는 Http Header와 Http Body를 포함하는 클래스
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        // RestTemplate
        // : Rest 방식 API를 호출할 수 있는 Spring 내장 클래스
        //   REST API 호출 이후 응답을 받을 때까지 기다리는 동기 방식 (json, xml 응답)
        RestTemplate template = new RestTemplate();
        String url = "https://open-api.kakaopay.com/online/v1/payment/ready";
        // RestTemplate의 postForEntity : POST 요청을 보내고 ResponseEntity로 결과를 반환받는 메소드
        ResponseEntity<ReadyResponse> responseEntity = template.postForEntity(url, requestEntity, ReadyResponse.class);

        ReadyResponse readyResponse = responseEntity.getBody();
        orderService.orderProcess(userId, readyResponse.getTid(), orderRequest);
        return readyResponse;
    }

    // 카카오페이 결제 승인
    // 사용자가 결제 수단을 선택하고 비밀번호를 입력해 결제 인증을 완료한 뒤,
    // 최종적으로 결제 완료 처리를 하는 단계
    @Transactional
    public ApproveResponse payApprove(String userId, String tid, String pgToken) throws Exception {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("cid", "TC0ONETIME");              // 테스트용 가맹점 코드
        parameters.put("tid", tid);                       // 결제 고유번호
        parameters.put("partner_order_id", "1234567890"); // 주문번호
        parameters.put("partner_user_id", "roommake");    // 회원 아이디
        parameters.put("pg_token", pgToken);              // 결제승인 요청 인증 토큰

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        RestTemplate template = new RestTemplate();
        String url = "https://open-api.kakaopay.com/online/v1/payment/approve";
        try {
            orderService.updateUserOrderStatus(userId, tid, "결제 완료");
            return template.postForObject(url, requestEntity, ApproveResponse.class);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    // 카카오페이 측에 요청 시 헤더부에 필요한 값
    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", secretKey);
        headers.set("Content-type", "application/json");

        return headers;
    }
}