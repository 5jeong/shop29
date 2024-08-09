package com.toy2.shop29.cart.domain;

import lombok.Getter;

@Getter
public class CartDto {

    private int product_id;
    private Integer quantity;
    private String product_name;
    private int price;
    private int sale_ratio;
    private String size_info;

    public CartDto() {}
    public CartDto(int product_id, int quantity, String product_name, int price, int sale_ratio) {
        this.product_id = product_id;
        this.quantity = quantity;
        this.product_name = product_name;
        this.price = price;
        this.sale_ratio = sale_ratio;
    }

    public int getProduct_id() {
        return product_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getProduct_name() {
        return product_name;
    }

    public int getPrice() {
        return price;
    }

    public int getSale_ratio() {
        return sale_ratio;
    }

    public String getSize_info() {
        return size_info;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setSale_ratio(int sale_ratio) {
        this.sale_ratio = sale_ratio;
    }

    public void setSize_info(String size_info) {
        this.size_info = size_info;
    }

    @Override
    public String toString() {
        return "CartDto{" +
                "product_id=" + product_id +
                ", quantity=" + quantity +
                ", product_name='" + product_name + '\'' +
                ", price=" + price +
                ", sale_ratio=" + sale_ratio +
                ", size_info='" + size_info + '\'' +
                '}';
    }
}
