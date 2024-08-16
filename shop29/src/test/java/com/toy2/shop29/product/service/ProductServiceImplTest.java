package com.toy2.shop29.product.service;

import com.toy2.shop29.product.domain.ProductDto;
import com.toy2.shop29.product.domain.ProductWithCategoriesDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductServiceImplTest {

    @Autowired
    private ProductService productService;


    @Test
    void read() {

    }


    @Test
    void getProductWithCategoriesTest() {
        int testProductId = 1;

        ProductWithCategoriesDto dto = productService.getProductWithCategories(testProductId);

        assertNotNull(dto, "Product with categories should not be null");
        assertEquals(testProductId, dto.getProductId(), "Product ID should match");
        assertEquals("MEN",dto.getMajorCategoryName());
        assertEquals("아우터",dto.getMiddleCategoryName());
        assertEquals("무스탕",dto.getSmallCategoryName());

    }








}