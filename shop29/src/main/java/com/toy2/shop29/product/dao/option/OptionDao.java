package com.toy2.shop29.product.dao.option;

import com.toy2.shop29.product.domain.option.OptionKeyValue;
import com.toy2.shop29.product.domain.option.ProductOptionDto;
import com.toy2.shop29.product.domain.option.ProductOptionValueDto;

import java.util.List;

public interface OptionDao {
    //해당 상품이 가지고 있는 옵션을 가져오는 메서드
    List<ProductOptionDto> findProductsOption(int productId);

    //해당 OptionKey가 가지고 있는 OptionValue 목록을 가져오는 메서드
    List<OptionKeyValue> findValuesByKeys(int optionId);

    //해당 상품이 가지고 있는 옵션value를 가져오는 메서드
    List<ProductOptionValueDto> findProductsOptionValue(int productId);
}
