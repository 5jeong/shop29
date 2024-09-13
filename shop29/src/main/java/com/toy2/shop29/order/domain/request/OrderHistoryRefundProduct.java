package com.toy2.shop29.order.domain.request;

import com.toy2.shop29.common.ProductItem;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class OrderHistoryRefundProduct {
    private String orderId;
    private List<ProductItem> productItems;
}
