package com.toy2.shop29.product.domain;

import java.util.Date;
import java.util.Objects;

public class SmallCategoryDto {
    private int small_category_id;
    private int middle_category_id;
    private String small_category_name;
    private Date created_date;
    private String created_id;
    private Date updated_date;
    private String updated_id;


    //생성자
    public SmallCategoryDto() {}

    public SmallCategoryDto(int small_category_id, int middle_category_id, String small_category_name, Date created_date, String created_id, Date updated_date, String updated_id) {
        this.small_category_id = small_category_id;
        this.middle_category_id = middle_category_id;
        this.small_category_name = small_category_name;
        this.created_date = created_date;
        this.created_id = created_id;
        this.updated_date = updated_date;
        this.updated_id = updated_id;
    }

    //toString
    @Override
    public String toString() {
        return "SmallCategoryDto{" +
                "small_category_id=" + small_category_id +
                ", middle_category_id=" + middle_category_id +
                ", small_category_name='" + small_category_name + '\'' +
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
        SmallCategoryDto that = (SmallCategoryDto) o;
        return small_category_id == that.small_category_id && middle_category_id == that.middle_category_id && Objects.equals(small_category_name, that.small_category_name) && Objects.equals(created_id, that.created_id) && Objects.equals(updated_id, that.updated_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(small_category_id, middle_category_id, small_category_name, created_id, updated_id);
    }

    //getter setter
    public int getSmall_category_id() {
        return small_category_id;
    }

    public void setSmall_category_id(int small_category_id) {
        this.small_category_id = small_category_id;
    }

    public int getMiddle_category_id() {
        return middle_category_id;
    }

    public void setMiddle_category_id(int middle_category_id) {
        this.middle_category_id = middle_category_id;
    }

    public String getSmall_category_name() {
        return small_category_name;
    }

    public void setSmall_category_name(String small_category_name) {
        this.small_category_name = small_category_name;
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
