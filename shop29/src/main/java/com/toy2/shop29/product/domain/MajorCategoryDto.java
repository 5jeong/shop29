package com.toy2.shop29.product.domain;

import java.util.Date;
import java.util.Objects;

public class MajorCategoryDto {
    private int major_category_id;
    private String major_category_name;
    private Date created_date;
    private String created_id;
    private Date updated_date;
    private String updated_id;


    //생성자
    public MajorCategoryDto() {
    }

    public MajorCategoryDto(int major_category_id, String major_category_name, Date created_date, String created_id, Date updated_date, String updated_id) {
        this.major_category_id = major_category_id;
        this.major_category_name = major_category_name;
        this.created_date = created_date;
        this.created_id = created_id;
        this.updated_date = updated_date;
        this.updated_id = updated_id;
    }

    //toString
    @Override
    public String toString() {
        return "MajorCategoryDto{" +
                "major_category_id=" + major_category_id +
                ", major_category_name='" + major_category_name + '\'' +
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
        MajorCategoryDto that = (MajorCategoryDto) o;
        return major_category_id == that.major_category_id && Objects.equals(major_category_name, that.major_category_name) && Objects.equals(created_id, that.created_id) && Objects.equals(updated_id, that.updated_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(major_category_id, major_category_name, created_id, updated_id);
    }

    //getter setter 
    public int getMajor_category_id() {
        return major_category_id;
    }

    public void setMajor_category_id(int major_category_id) {
        this.major_category_id = major_category_id;
    }

    public String getMajor_category_name() {
        return major_category_name;
    }

    public void setMajor_category_name(String major_category_name) {
        this.major_category_name = major_category_name;
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
