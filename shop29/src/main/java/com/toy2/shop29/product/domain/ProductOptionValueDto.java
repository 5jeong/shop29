package com.toy2.shop29.product.domain;

import java.util.Date;
import java.util.Objects;

public class ProductOptionValueDto {
    private int product_option_value_key;
    private int product_id;
    private int option_value_id;
    private int extra_fee;
    private int stock;
    private Date created_date;
    private String created_id;
    private Date updated_date;
    private String updated_id;

    //생성자
    public ProductOptionValueDto() {}

    public ProductOptionValueDto(int product_option_value_key, int product_id, int option_value_id, int extra_fee, int stock, Date created_date, String created_id, Date updated_date, String updated_id) {
        this.product_option_value_key = product_option_value_key;
        this.product_id = product_id;
        this.option_value_id = option_value_id;
        this.extra_fee = extra_fee;
        this.stock = stock;
        this.created_date = created_date;
        this.created_id = created_id;
        this.updated_date = updated_date;
        this.updated_id = updated_id;
    }

    //toString
    @Override
    public String toString() {
        return "ProductOptionValueDto{" +
                "product_option_value_key=" + product_option_value_key +
                ", product_id=" + product_id +
                ", option_value_id=" + option_value_id +
                ", extra_fee=" + extra_fee +
                ", stock=" + stock +
                ", created_date=" + created_date +
                ", created_id='" + created_id + '\'' +
                ", updated_date=" + updated_date +
                ", updated_id='" + updated_id + '\'' +
                '}';
    }

    //equals hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductOptionValueDto that = (ProductOptionValueDto) o;
        return product_option_value_key == that.product_option_value_key && product_id == that.product_id && option_value_id == that.option_value_id && extra_fee == that.extra_fee && stock == that.stock && Objects.equals(created_id, that.created_id) && Objects.equals(updated_id, that.updated_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product_option_value_key, product_id, option_value_id, extra_fee, stock, created_id, updated_id);
    }

    //getter setter
    public int getProduct_option_value_key() {
        return product_option_value_key;
    }

    public void setProduct_option_value_key(int product_option_value_key) {
        this.product_option_value_key = product_option_value_key;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getOption_value_id() {
        return option_value_id;
    }

    public void setOption_value_id(int option_value_id) {
        this.option_value_id = option_value_id;
    }

    public int getExtra_fee() {
        return extra_fee;
    }

    public void setExtra_fee(int extra_fee) {
        this.extra_fee = extra_fee;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public Date getCreated_date() {
        return created_date;
    }

    public void setCreated_date(Date created_date) {
        this.created_date = created_date;
    }

    public String getCreated_id() {
        return created_id;
    }

    public void setCreated_id(String created_id) {
        this.created_id = created_id;
    }

    public Date getUpdated_date() {
        return updated_date;
    }

    public void setUpdated_date(Date updated_date) {
        this.updated_date = updated_date;
    }

    public String getUpdated_id() {
        return updated_id;
    }

    public void setUpdated_id(String updated_id) {
        this.updated_id = updated_id;
    }
}
