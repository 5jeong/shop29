package com.toy2.shop29.product.domain.option;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ProductOptionValueDto {
    private int productOptionValueId;
    private int productId;
    private int optionValueId;
    private int optionKeyId;
    private String optionValueName;
    private int extraFee;
    private int stock;
    private Date createdDate;
    private String createdId;
    private Date updatedDate;
    private String updatedId;
}
