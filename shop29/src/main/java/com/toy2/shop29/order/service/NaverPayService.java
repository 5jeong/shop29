package com.toy2.shop29.order.service;

import com.toy2.shop29.order.domain.pay.KakaoPayApproveResponseDTO;
import com.toy2.shop29.order.domain.pay.KakaoPayReadyResponseDto;
import com.toy2.shop29.order.domain.request.OrderCompletedRequestDTO;

public interface NaverPayService {
    KakaoPayReadyResponseDto payReady(String userId, OrderCompletedRequestDTO orderRequest) throws Exception;
    KakaoPayApproveResponseDTO payApprove(String userId, String tid, String pgToken) throws Exception;
}
