package com.toy2.shop29.product.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Objects;

@Getter
@Setter
public class ProductDto {
    //총 11가지
    private int productId;
    private int smallCategoryId;
    private String productName;
    private int brandId;
    private int price;
    private int saleRatio;
    private float rating;
    private int isExclusive;
    private String sizeTable;
    private Date startDate;
    private Date createdDate;
    private String createdId;
    private Date updatedDate;
    private String updatedId;


    public ProductDto(int productId, int smallCategoryId, String productName, int brandId, int price, int saleRatio, float rating, int isExclusive, String createdId, String updatedId) {
        this.productId = productId;
        this.smallCategoryId = smallCategoryId;
        this.productName = productName;
        this.brandId = brandId;
        this.price = price;
        this.saleRatio = saleRatio;
        this.rating = rating;
        this.isExclusive = isExclusive;
        this.createdId = createdId;
        this.updatedId = updatedId;
    }
}
