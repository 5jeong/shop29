package com.toy2.shop29.product.dao;

import com.toy2.shop29.product.domain.CategoryDto;
import com.toy2.shop29.product.domain.MajorCategoryDto;
import com.toy2.shop29.product.domain.MiddleCategoryDto;
import com.toy2.shop29.product.domain.SmallCategoryDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CategoryDaoImplTest {

    @Autowired
    private CategoryDao categoryDao;


    @Test
    void findMiddleCategoriesByMajorId() {
    }

    @Test
    void findSmallCategoriesByMiddleId() {
    }


    @Test
    void findProductsBySmallId() {
    }

    @Test
    void getCategoryHierarchy() {
        CategoryDto categoryDto = new CategoryDto();

        categoryDto.setMajorCategory(new MajorCategoryDto(
                1,"MEN","aaa","aaa"
        ));
        categoryDto.setMiddleCategory(new MiddleCategoryDto(
                1,1,"아우터","aaa","aaa"
        ));
        categoryDto.setSmallCategory(new SmallCategoryDto(
                1,1,"무스탕","aaa","aaa"
        ));

    }




    @Test
    void findAllCategories() {

        // When
        List<CategoryDto> categories = categoryDao.findAllCategories();

        // Then
        assertNotNull(categories); // 리스트가 null이 아닌지 확인
        assertTrue(categories.size() > 0); // 최소한 하나 이상의 카테고리가 반환되는지 확인

        // 예를 들어 첫 번째 카테고리를 확인 (데이터베이스에 실제 데이터가 있어야 함)
        CategoryDto firstCategory = categories.get(0);
        assertNotNull(firstCategory.getMajorCategory());
        assertNotNull(firstCategory.getMajorCategory().getMajorCategoryId());
        assertNotNull(firstCategory.getMajorCategory().getMajorCategoryName());

        assertNotNull(firstCategory.getMiddleCategory());
        assertNotNull(firstCategory.getMiddleCategory().getMiddleCategoryId());
        assertNotNull(firstCategory.getMiddleCategory().getMiddleCategoryName());

        assertNotNull(firstCategory.getSmallCategory());
        assertNotNull(firstCategory.getSmallCategory().getSmallCategoryId());
        assertNotNull(firstCategory.getSmallCategory().getSmallCategoryName());

    }


}




