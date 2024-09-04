package com.toy2.shop29.chatBot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class ChatBotService {
    private final ObjectMapper objectMapper;

    @Transactional
    public String sendMessage(String userInfo, String message) {
        RestTemplate restTemplate = new RestTemplate();

        // 헤더를 form-data로 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // MultiValueMap을 사용하여 form 데이터를 생성
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("userInfo", userInfo);
        map.add("message", message);

        // HttpEntity를 사용하여 헤더와 바디 설정
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

        // 실제 Flask 서버와 연결하기 위한 URL
        String url = "http://0.0.0.0:8082/chat";

        // Flask 서버로 데이터를 전송하고 받은 응답 값을 반환
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
        System.out.println(response.getBody());
        return response.getBody();
    }

    @Transactional
    public String getOrderHistory(String userId, String orderId) {
        RestTemplate restTemplate = new RestTemplate();

        // 실제 Flask 서버와 연결하기 위한 URL
        String baseUrl = "http://0.0.0.0:8082/order-history";

        String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("userId", userId)
                .queryParam("orderId", orderId)
                .toUriString();

        // Flask 서버로 데이터를 전송하고 받은 응답 값을 반환
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        System.out.println(response.getBody());
        return response.getBody();
    }

    @Transactional
    public String getRefundableOrderList(String userId) {
        RestTemplate restTemplate = new RestTemplate();

        // 실제 Flask 서버와 연결하기 위한 URL
        String baseUrl = "http://0.0.0.0:8082/refund";

        String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("userId", userId)
                .toUriString();

        // Flask 서버로 데이터를 전송하고 받은 응답 값을 반환
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        System.out.println(response.getBody());
        return response.getBody();
    }

    @Transactional
    public String getRefundableOrder(String userId, String orderId) {
        RestTemplate restTemplate = new RestTemplate();

        // 실제 Flask 서버와 연결하기 위한 URL
        String baseUrl = "http://0.0.0.0:8082/refund";

        String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("userId", userId)
                .queryParam("orderId", orderId)
                .toUriString();
        // Flask 서버로 데이터를 전송하고 받은 응답 값을 반환
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        System.out.println(response.getBody());
        return response.getBody();
    }

    @Transactional
    public String getProductSearcher() {

        RestTemplate restTemplate = new RestTemplate();

        // 실제 Flask 서버와 연결하기 위한 URL
        String baseUrl = "http://0.0.0.0:8082/product";

        String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .toUriString();
        // Flask 서버로 데이터를 전송하고 받은 응답 값을 반환
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        System.out.println(response.getBody());
        return response.getBody();
    }

    @Transactional
    public String getProductInformation(String productId) {

        RestTemplate restTemplate = new RestTemplate();

        // 실제 Flask 서버와 연결하기 위한 URL
        String baseUrl = "http://0.0.0.0:8082/product";

        String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("productId", productId)
                .toUriString();
        // Flask 서버로 데이터를 전송하고 받은 응답 값을 반환
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        System.out.println(response.getBody());
        return response.getBody();
    }
}
