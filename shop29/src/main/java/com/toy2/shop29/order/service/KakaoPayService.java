package com.toy2.shop29.order.service;

import com.toy2.shop29.order.domain.pay.ReadyResponse;
import com.toy2.shop29.order.domain.request.OrderCompletedRequestDTO;

public interface KakaoPayService {
    ReadyResponse payReady(String userId, OrderCompletedRequestDTO orderRequest) throws Exception;
    ApproveResponse payApprove(String userId, String tid, String pgToken) throws Exception;
}
