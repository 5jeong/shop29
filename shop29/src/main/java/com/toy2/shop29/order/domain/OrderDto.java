package com.toy2.shop29.order.domain;


import java.util.Date;

public class OrderDto {
    private long order_id;
    private Date order_date;
    private long total_amount;
    private String status;
    private boolean is_user;
    private String email;

    public OrderDto() {}

    public OrderDto(long order_id, Date order_date, long total_amount, String status, boolean is_user) {
        this.order_id = order_id;
        this.order_date = order_date;
        this.total_amount = total_amount;
        this.status = status;
        this.is_user = is_user;
    }

    public long getOrder_id() {
        return order_id;
    }

    public void setOrder_id(long order_id) {
        this.order_id = order_id;
    }

    public Date getOrder_date() {
        return order_date;
    }

    public void setOrder_date(Date order_date) {
        this.order_date = order_date;
    }

    public long getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(long total_amount) {
        this.total_amount = total_amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isIs_user() {
        return is_user;
    }

    public void setIs_user(boolean is_user) {
        this.is_user = is_user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getId() {
        return order_id;
    }
}
