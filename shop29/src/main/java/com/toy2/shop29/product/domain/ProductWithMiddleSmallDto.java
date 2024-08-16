package com.toy2.shop29.product.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ProductWithMiddleSmallDto {
    // Middle Small의 이름뿐아니라 Id도 가져옴

    private int productId;
    private String productName;
    private int price;
    private int saleRatio;
    private float rating;
    private int isExclusive;
    private String sizeTable;
    private String middleCategoryName;
    private int middleCategoryId;
    private String smallCategoryName;
    private int smallCategoryId;
    private int brandId;
    private Date startDate;
    private Date createdDate;
    private String createdId;
    private Date updatedDate;
    private String updatedId;


}
