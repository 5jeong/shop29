package com.toy2.shop29.product.dao;

import com.toy2.shop29.product.domain.CategoryDto;
import com.toy2.shop29.product.domain.MajorCategoryDto;
import com.toy2.shop29.product.domain.MiddleCategoryDto;
import com.toy2.shop29.product.domain.SmallCategoryDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CategoryDaoImplTest {

    @Autowired
    private CategoryDao categoryDao;

    @Test
    void findAllMajorCategories() {
//        int categories = categoryDao.findAllMajorCategories().size();
//        assertTrue(2==categories);
        assertTrue(categoryDao!=null);
    }

    @Test
    void findMiddleCategoriesByMajorId() {
    }

    @Test
    void findSmallCategoriesByMiddleId() {
    }

    @Test
    void findAllCategories() {
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
    void deleteAllCategories() {
//        categoryDao.insertSmallCategory();
//        categoryDao.insertMiddleCategory();
//        categoryDao.insertSmallCategory();
    }








}