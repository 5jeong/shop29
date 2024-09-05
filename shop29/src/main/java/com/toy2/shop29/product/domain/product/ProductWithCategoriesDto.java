package com.toy2.shop29.product.domain.product;

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
    private int majorCategoryId;  // 변경: String -> int
    private int middleCategoryId; // 변경: String -> int
    private int smallCategoryId;  // 변경: String -> int
    private int brandId;
    private String brandName;
    private Date startDate;
    private Date createdDate;
    private String createdId;
    private Date updatedDate;
    private String updatedId;

    // Getter and Setter methods
    // ...
}
