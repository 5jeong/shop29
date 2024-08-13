package com.toy2.shop29.product.domain;

import java.util.Date;
import java.util.Objects;


public class ProductDto {
    //총 11가지
    private int product_id;
    private int small_category_id;
    private String product_name;
    private int brand_id;
    private int price;
    private int sale_ratio;
    private float rating;
    private int is_exclusive;
    private String size_table;
    private Date start_date;
    private Date created_date;
    private String created_id;
    private Date updated_date;
    private String updated_id;


    //생성자
    public ProductDto() {}

    public ProductDto(int product_id, int small_category_id, String product_name, int brand_id, int price, int sale_ratio, float rating, int is_exclusive, String size_table, Date start_date, Date created_date, String created_id, Date updated_date, String updated_id) {
        this.product_id = product_id;
        this.small_category_id = small_category_id;
        this.product_name = product_name;
        this.brand_id = brand_id;
        this.price = price;
        this.sale_ratio = sale_ratio;
        this.rating = rating;
        this.is_exclusive = is_exclusive;
        this.size_table = size_table;
        this.start_date = start_date;
        this.created_date = created_date;
        this.created_id = created_id;
        this.updated_date = updated_date;
        this.updated_id = updated_id;
    }

    //toString
    @Override
    public String toString() {
        return "ProductDto{" +
                "product_id=" + product_id +
                ", small_category_id=" + small_category_id +
                ", product_name='" + product_name + '\'' +
                ", brand_id=" + brand_id +
                ", price=" + price +
                ", sale_ratio=" + sale_ratio +
                ", rating=" + rating +
                ", is_exclusive=" + is_exclusive +
                ", size_table='" + size_table + '\'' +
                ", start_date=" + start_date +
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
        ProductDto that = (ProductDto) o;
        return product_id == that.product_id && small_category_id == that.small_category_id && brand_id == that.brand_id && price == that.price && sale_ratio == that.sale_ratio && Float.compare(rating, that.rating) == 0 && is_exclusive == that.is_exclusive && Objects.equals(product_name, that.product_name) && Objects.equals(size_table, that.size_table) && Objects.equals(start_date, that.start_date) && Objects.equals(created_id, that.created_id) && Objects.equals(updated_id, that.updated_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product_id, small_category_id, product_name, brand_id, price, sale_ratio, rating, is_exclusive, size_table, start_date, created_id, updated_id);
    }

    //getter setter
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

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public int getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(int brand_id) {
        this.brand_id = brand_id;
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

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getIs_exclusive() {
        return is_exclusive;
    }

    public void setIs_exclusive(int is_exclusive) {
        this.is_exclusive = is_exclusive;
    }

    public String getSize_table() {
        return size_table;
    }

    public void setSize_table(String size_table) {
        this.size_table = size_table;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
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
