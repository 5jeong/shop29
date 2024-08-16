package com.toy2.shop29.product.domain;

import java.util.Date;
import java.util.Objects;

public class ProductOptionDto {
    private int product_option_id;
    private int product_id;
    private int option_key_id;
    private Date created_date;
    private String created_id;
    private Date updated_date;
    private String updated_id;

    //생성자
    public ProductOptionDto() {}

    public ProductOptionDto(int product_option_id, int product_id, int option_key_id, Date created_date, String created_id, Date updated_date, String updated_id) {
        this.product_option_id = product_option_id;
        this.product_id = product_id;
        this.option_key_id = option_key_id;
        this.created_date = created_date;
        this.created_id = created_id;
        this.updated_date = updated_date;
        this.updated_id = updated_id;
    }

    //toString
    @Override
    public String toString() {
        return "ProductOptionDto{" +
                "product_option_id=" + product_option_id +
                ", product_id=" + product_id +
                ", option_key_id=" + option_key_id +
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
        ProductOptionDto that = (ProductOptionDto) o;
        return product_option_id == that.product_option_id && product_id == that.product_id && option_key_id == that.option_key_id && Objects.equals(created_id, that.created_id) && Objects.equals(updated_id, that.updated_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product_option_id, product_id, option_key_id, created_id, updated_id);
    }

    //getter setter
    public int getProduct_option_id() {
        return product_option_id;
    }

    public void setProduct_option_id(int product_option_id) {
        this.product_option_id = product_option_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getOption_key_id() {
        return option_key_id;
    }

    public void setOption_key_id(int option_key_id) {
        this.option_key_id = option_key_id;
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
