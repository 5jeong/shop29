package com.toy2.shop29.cart.domain.request;

public class AddCartProductDto {
    private Long productId;
    private Long qauntity;

    public Long getQauntity() {
        return qauntity;
    }

    public void setQauntity(Long qauntity) {
        this.qauntity = qauntity;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

}
