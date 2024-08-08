package com.toy2.shop29.product.domain;

import java.util.Date;
import java.util.Objects;


public class ProductDto {
    //총 11가지
    private int product_id;
    private String brand_id;
    private int small_category_id;
    private String product_name;
    private int price;
    private int sale_ratio;
    private int is_exclusive;
    private Date created_date;
    private String created_id;
    private Date updated_date;
    private String updated_id;


    // 1.equals, hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductDto productDto = (ProductDto) o;
        return product_id == productDto.product_id && small_category_id == productDto.small_category_id && price == productDto.price && sale_ratio == productDto.sale_ratio && is_exclusive == productDto.is_exclusive && Objects.equals(brand_id, productDto.brand_id) && Objects.equals(product_name, productDto.product_name) && Objects.equals(created_id, productDto.created_id) && Objects.equals(updated_id, productDto.updated_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product_id, small_category_id, brand_id, product_name, price, sale_ratio, is_exclusive, created_id, updated_id);
    }

    // 2.toString
    @Override
    public String toString() {
        return "Product{" +
                "product_id=" + product_id +
                ", small_category_id=" + small_category_id +
                ", brand_id='" + brand_id + '\'' +
                ", product_name='" + product_name + '\'' +
                ", price=" + price +
                ", sale_ratio=" + sale_ratio +
                ", is_exclusive=" + is_exclusive +
                ", created_date=" + created_date +
                ", created_id='" + created_id + '\'' +
                ", updated_date=" + updated_date +
                ", updated_id='" + updated_id + '\'' +
                '}';
    }

    // 3.생성자
    public ProductDto() {}

    public ProductDto(int product_id, int small_category_id, String brand_id, String product_name, int price, int sale_ratio, int is_exclusive, Date created_date, String created_id, Date updated_date, String updated_id) {
        this.product_id = product_id;
        this.small_category_id = small_category_id;
        this.brand_id = brand_id;
        this.product_name = product_name;
        this.price = price;
        this.sale_ratio = sale_ratio;
        this.is_exclusive = is_exclusive;
        this.created_date = created_date;
        this.created_id = created_id;
        this.updated_date = updated_date;
        this.updated_id = updated_id;
    }


    // 4.getter setter
    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getSmall_category_id() {
        return small_category_id;
    }

    public void setSmall_category_id(int small_category_id) {
        this.small_category_id = small_category_id;
    }

    public String getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(String brand_id) {
        this.brand_id = brand_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getSale_ratio() {
        return sale_ratio;
    }

    public void setSale_ratio(int sale_ratio) {
        this.sale_ratio = sale_ratio;
    }

    public int getIs_exclusive() {
        return is_exclusive;
    }

    public void setIs_exclusive(int is_exclusive) {
        this.is_exclusive = is_exclusive;
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
