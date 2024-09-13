package com.toy2.shop29.order.domain.pay;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Getter
@Setter
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoPayCancelResponseDTO {
    private String aid;                            // 요청 고유 번호
    private String tid;                            // 결제 고유 번호
    private String cid;                            // 가맹점 코드
    private PaymentStatus status;                  // 결제 상태
    private String partnerOrderId;                 // 가맹점 주문번호
    private String partnerUserId;                  // 가맹점 회원 id
    private String paymentMethodType;              // 결제 수단, CARD 또는 MONEY 중 하나
    private AmountDetail amount;                   // 결제 금액
    private AmountDetail approvedCancelAmount;     // 이번 요청으로 취소된 금액
    private AmountDetail canceledAmount;           // 누계 취소 금액
    private AmountDetail cancelAvailableAmount;    // 남은 취소 가능 금액
    private String itemName;                       // 상품 이름
    private String itemCode;                       // 상품 코드
    private int quantity;                          // 상품 수량
    private String createdAt;                      // 결제 준비 요청 시각
    private String approvedAt;                     // 결제 승인 시각
    private String canceledAt;                     // 결제 취소 시각
    private String payload;                        // 결제 승인 요청에 대해 저장한 값, 요청 시 전달된 내용
}