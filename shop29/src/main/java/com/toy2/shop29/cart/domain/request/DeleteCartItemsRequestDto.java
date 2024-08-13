package com.toy2.shop29.cart.domain.request;

import java.util.List;

public class DeleteCartItemsRequestDto {
    private List<Long> productIds;

    public List<Long> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<Long> productIds) {
        this.productIds = productIds;
    }

}
