package com.toy2.shop29.product.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ProductWithCategoriesDto {
    private int productId;
    private String productName;
    private int price;
    private int saleRatio;
    private float rating;
    private int isExclusive;
    private String sizeTable;
    private String majorCategoryName;
    private String middleCategoryName;
    private String smallCategoryName;
    private int brandId;
    private Date startDate;
    private Date createdDate;
    private String createdId;
    private Date updatedDate;
    private String updatedId;

}
