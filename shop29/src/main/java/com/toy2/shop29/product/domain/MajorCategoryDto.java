package com.toy2.shop29.product.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class MajorCategoryDto {
    private int majorCategoryId;
    private String majorCategoryName;
    private Date createdDate;
    private String createdId;
    private Date updatedDate;
    private String updatedId;


    public MajorCategoryDto() {

    }


    public MajorCategoryDto(int majorCategoryId, String majorCategoryName, String createdId, String updatedId) {
        this.majorCategoryId = majorCategoryId;
        this.majorCategoryName = majorCategoryName;
        this.createdId = createdId;
        this.updatedId = updatedId;
    }


}
