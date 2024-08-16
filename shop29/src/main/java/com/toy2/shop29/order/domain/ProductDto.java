package com.toy2.shop29.order.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
// TODO : 삭제 예정
public class ProductDto {
    private int productId;
    private String productName;
    private int smallCategoryId;
    private String brandId;
    private int price;
    private int saleRatio;
    private int isExclusive;
}
