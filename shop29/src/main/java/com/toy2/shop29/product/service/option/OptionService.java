package com.toy2.shop29.product.service.option;

import com.toy2.shop29.product.domain.option.OptionKeyValue;
import com.toy2.shop29.product.domain.option.ProductOptionDto;
import com.toy2.shop29.product.domain.option.ProductOptionValueDto;

import java.util.List;

public interface OptionService {

    List<ProductOptionDto> getProductOptions(int productId);

    List<OptionKeyValue> getOptionValuesByKey(int optionKeyId);

    List<ProductOptionValueDto> getProductOptionValues(int productId);
}
