package com.toy2.shop29.order.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderHistoryItemDto {
    private Long orderItemId;
    private String orderId;
    private String userId;
    private Long productId;
    private Long quantity;
    private Long productOptionId;
    private Long productPrice;
    private String refundStatus;
    private Date refundTime;
}
