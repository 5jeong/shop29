package com.toy2.shop29.product.domain.option;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class OptionKeyValue {
    private int optionKeyId;
    private String optionKeyName;
    private int optionValueId;
    private String optionValueName;
    private Date createdDate;
    private String createdId;
    private Date updatedDate;
    private String updatedId;

}
