package com.toy2.shop29.product.domain;

import java.util.Date;
import java.util.Objects;

public class OptionValueDto {
    private int option_value_id;
    private int option_id;
    private String option_value_name;
    private Date created_date;
    private String created_id;
    private Date updated_date;
    private String updated_id;


    //생성자
    public OptionValueDto() {}

    public OptionValueDto(int option_value_id, int option_id, String option_value_name, Date created_date, String created_id, Date updated_date, String updated_id) {
        this.option_value_id = option_value_id;
        this.option_id = option_id;
        this.option_value_name = option_value_name;
        this.created_date = created_date;
        this.created_id = created_id;
        this.updated_date = updated_date;
        this.updated_id = updated_id;
    }

    //toString
    @Override
    public String toString() {
        return "OptionValueDto{" +
                "option_value_id=" + option_value_id +
                ", option_id=" + option_id +
                ", option_value_name='" + option_value_name + '\'' +
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
        OptionValueDto that = (OptionValueDto) o;
        return option_value_id == that.option_value_id && option_id == that.option_id && Objects.equals(option_value_name, that.option_value_name) && Objects.equals(created_id, that.created_id) && Objects.equals(updated_id, that.updated_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(option_value_id, option_id, option_value_name, created_id, updated_id);
    }

    //getter setter
    public int getOption_value_id() {
        return option_value_id;
    }

    public void setOption_value_id(int option_value_id) {
        this.option_value_id = option_value_id;
    }

    public int getOption_id() {
        return option_id;
    }

    public void setOption_id(int option_id) {
        this.option_id = option_id;
    }

    public String getOption_value_name() {
        return option_value_name;
    }

    public void setOption_value_name(String option_value_name) {
        this.option_value_name = option_value_name;
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
