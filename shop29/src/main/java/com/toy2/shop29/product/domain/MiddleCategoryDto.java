package com.toy2.shop29.product.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Setter
@Getter
public class MiddleCategoryDto {
    private int middleCategoryId;
    private int majorCategoryId;
    private String middleCategoryName;
    private Date createdDate;
    private String createdId;
    private Date updatedDate;
    private String updatedId;

    public MiddleCategoryDto() {

    }

    public MiddleCategoryDto(int middleCategoryId, int majorCategoryId, String middleCategoryName, String createdId, String updatedId) {
        this.middleCategoryId = middleCategoryId;
        this.majorCategoryId = majorCategoryId;
        this.middleCategoryName = middleCategoryName;
        this.createdId = createdId;
        this.updatedId = updatedId;
    }

}
