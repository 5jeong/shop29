package com.toy2.shop29.product.dao.option;

import com.toy2.shop29.product.domain.option.ProductOptionValueDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
public class OptionDaoImplTest {

    @Autowired
    private OptionDao optionDao;

    @Test
    public void testFindProductsOptionValue() {
        int productId = 10100;  // 테스트할 상품 ID
        List<ProductOptionValueDto> optionValues = optionDao.findProductsOptionValue(productId);

        // 기대하는 값이 존재하는지 확인
        assertNotNull(optionValues);
        assertFalse(optionValues.isEmpty());

        // 추가적인 검증을 위해 각 요소를 확인할 수 있습니다.
        for (ProductOptionValueDto value : optionValues) {
            System.out.println("Option Value ID: " + value.getOptionValueId());
            System.out.println("Option Value Name: " + value.getOptionValueName());
        }
    }
}