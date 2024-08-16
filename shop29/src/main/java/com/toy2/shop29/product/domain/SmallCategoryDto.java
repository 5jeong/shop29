package com.toy2.shop29.product.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Objects;

@Getter
@Setter
public class SmallCategoryDto {
    private int smallCategoryId;
    private int middleCategoryId;
    private String smallCategoryName;
    private Date createdDate;
    private String createdId;
    private Date updatedDate;
    private String updatedId;



    public SmallCategoryDto() {

    }


    public SmallCategoryDto(int smallCategoryId, int middleCategoryId, String smallCategoryName, String createdId, String updatedId) {
        this.smallCategoryId = smallCategoryId;
        this.middleCategoryId = middleCategoryId;
        this.smallCategoryName = smallCategoryName;
        this.createdId = createdId;
        this.updatedId = updatedId;
    }


}
