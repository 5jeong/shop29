package com.toy2.shop29.product.domain;

import java.util.Objects;

public class ProductStockDto {
    private int product_id;
    private String option_type;
    private String option_value;
    private int stock;
    private int extra_fee;



    //생성자
    public ProductStockDto() {}

    public ProductStockDto(int product_id, String option_type, String option_value, int stock, int extra_fee) {
        this.product_id = product_id;
        this.option_type = option_type;
        this.option_value = option_value;
        this.stock = stock;
        this.extra_fee = extra_fee;
    }

    //toString
    @Override
    public String toString() {
        return "ProductStockDto{" +
                "product_id=" + product_id +
                ", option_type='" + option_type + '\'' +
                ", option_value='" + option_value + '\'' +
                ", stock=" + stock +
                ", extra_fee=" + extra_fee +
                '}';
    }

    //equals hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductStockDto that = (ProductStockDto) o;
        return product_id == that.product_id && stock == that.stock && extra_fee == that.extra_fee && Objects.equals(option_type, that.option_type) && Objects.equals(option_value, that.option_value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product_id, option_type, option_value, stock, extra_fee);
    }

    //getter setter
    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getOption_type() {
        return option_type;
    }

    public void setOption_type(String option_type) {
        this.option_type = option_type;
    }

    public String getOption_value() {
        return option_value;
    }

    public void setOption_value(String option_value) {
        this.option_value = option_value;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getExtra_fee() {
        return extra_fee;
    }

    public void setExtra_fee(int extra_fee) {
        this.extra_fee = extra_fee;
    }
}
