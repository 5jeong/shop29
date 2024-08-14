package com.toy2.shop29.order.service;

import com.toy2.shop29.order.domain.pay.ApproveResponseDTO;
import com.toy2.shop29.order.domain.pay.ReadyResponseDto;
import com.toy2.shop29.order.domain.request.OrderCompletedRequestDTO;

public interface KakaoPayService {
    ReadyResponseDto payReady(String userId, OrderCompletedRequestDTO orderRequest) throws Exception;
    ApproveResponseDTO payApprove(String userId, String tid, String pgToken) throws Exception;
}
