package com.toy2.shop29.product.service.option;

import com.toy2.shop29.product.dao.option.OptionDao;
import com.toy2.shop29.product.domain.option.OptionKeyValue;
import com.toy2.shop29.product.domain.option.ProductOptionDto;
import com.toy2.shop29.product.domain.option.ProductOptionValueDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OptionServiceImpl implements com.toy2.shop29.product.service.option.OptionService {

    @Autowired
    private OptionDao optionDao;


    //
    @Override public List<ProductOptionDto> getProductOptions(int productId) {
        return optionDao.findProductsOption(productId);
    }


    @Override public List<OptionKeyValue> getOptionValuesByKey(int optionKeyId) {
        return optionDao.findValuesByKeys(optionKeyId);
    }


    @Override public List<ProductOptionValueDto> getProductOptionValues(int productId) {
        return optionDao.findProductsOptionValue(productId);
    }
}
