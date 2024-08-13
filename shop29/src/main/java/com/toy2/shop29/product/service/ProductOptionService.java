package com.toy2.shop29.product.service;


import com.toy2.shop29.product.dao.ProductOptionDao;
import com.toy2.shop29.product.domain.ProductStockDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductOptionService {

    @Autowired
    private ProductOptionDao productOptionDao;

    public List<ProductStockDto> getProductStockByOptions(int productId, String optionType, String optionValue) {
        Map<String, Object> params = new HashMap<>();
        params.put("productId", productId);
        params.put("optionType", optionType);
        params.put("optionValue", optionValue);

        return productOptionDao.selectProductStockByOptions(params);
    }
}