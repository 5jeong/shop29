package com.toy2.shop29.order.domain.request;

import jakarta.validation.constraints.NotNull;

public class OrderProductDto {
    @NotNull()
    public Long productId;
    @NotNull()
    public Long quantity;

    public @NotNull() Long getProductId() {
        return productId;
    }

    public void setProductId(@NotNull() Long productId) {
        this.productId = productId;
    }

    public @NotNull() Long getQuantity() {
        return quantity;
    }

    public void setQuantity(@NotNull() Long quantity) {
        this.quantity = quantity;
    }
}
