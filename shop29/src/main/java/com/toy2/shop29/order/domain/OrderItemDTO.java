package com.toy2.shop29.order.domain;

public class OrderItemDTO {
    private Long productId;
    private String productName;
    private Long quantity;
    private Long price;
    private int saleRatio;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public int getSaleRatio() {
        return saleRatio;
    }

    public void setSaleRatio(int saleRatio) {
        this.saleRatio = saleRatio;
    }
}
