package com.toy2.shop29.product.dao;

import com.toy2.shop29.product.domain.ProductDto;
import com.toy2.shop29.product.domain.ProductWithCategoriesDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductDaoImplTest {

    @Autowired
    private ProductDao productDao;


    @Test
    void insert() {
        if(productDao!=null) {
            productDao.deleteAll();
        }

        for(int i=1;i<=460;i++) {
            ProductDto productDto = new ProductDto(i,1,"product"+i,1,1000*i,0,0,0,"aaaa","aaa");
            productDao.insert(productDto);
        }


    }

    @Test
    void select() {
    }

    @Test
    void selectAll() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @Test
    void deleteAll() {
    }

    @Test
    void count() {
    }

    @Test
    void selectPage() {
    }

    @Test
    void selectProductByCategory() {
    }

    @Test
    void selectProductByBrand() {
    }

    @Test
    void selectProductByPriceRange() {
    }

    @Test
    void sortedByPriceDesc() {
    }

    @Test
    void sortedByPriceAsc() {
    }

    @Test
    void sortedByNew() {
    }

    @Test
    void sortedByHighDiscount() {
    }


    @Test
    void selectProductWithCategoriesTest() {
        // 테스트를 위한 productId 설정
        int testProductId = 1;

        // 실제 메서드 호출 (mysql에 있는지 확인)
        ProductWithCategoriesDto dto = productDao.selectProductWithCategories(testProductId);

        // 검증
        assertEquals(testProductId, dto.getProductId(), "Product ID should match");
        // 필요한 경우 더 많은 필드를 검증할 수 있습니다.
        assertEquals("MEN",dto.getMajorCategoryName());
        assertEquals("아우터",dto.getMiddleCategoryName());
        assertEquals("무스탕",dto.getSmallCategoryName());

    }




}