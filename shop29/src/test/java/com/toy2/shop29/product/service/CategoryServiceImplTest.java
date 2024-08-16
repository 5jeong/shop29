package com.toy2.shop29.product.service;

import com.toy2.shop29.product.dao.CategoryDao;
import com.toy2.shop29.product.domain.CategoryDto;
import com.toy2.shop29.product.domain.MajorCategoryDto;
import com.toy2.shop29.product.domain.MiddleCategoryDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;




@SpringBootTest
class CategoryServiceImplTest {

    @Autowired
    private CategoryService categoryService;



    @Test
    void getAllCategories() {

        // When
        List<CategoryDto> categories = categoryService.getAllCategories();

        // Then
        assertNotNull(categories);


        assertFalse(categories.isEmpty(), "Category list should not be empty.");

        // 특정 카테고리에 대해 확인하고 싶다면
        CategoryDto firstCategory = categories.get(0);
        assertNotNull(firstCategory.getMajorCategory().getMajorCategoryName(), "Major Category Name should not be null.");
        assertNotNull(firstCategory.getMiddleCategory().getMiddleCategoryName(), "Middle Category Name should not be null.");


        assertNotNull(firstCategory.getSmallCategory().getSmallCategoryName(), "Small Category Name should not be null.");

    }



    @Test
    public void testGetMajorToMiddleCategoryMap() {

        Map<MajorCategoryDto, List<MiddleCategoryDto>> majorToMiddleMap = categoryService.getMajorToMiddleCategoryMap();

        // Assert: 대분류 데이터가 제대로 생성되었는지 확인
        assertNotNull(majorToMiddleMap, "Major to Middle Category map should not be null");
        assertFalse(majorToMiddleMap.isEmpty(), "Major to Middle Category map should not be empty");

        for (Map.Entry<MajorCategoryDto, List<MiddleCategoryDto>> entry : majorToMiddleMap.entrySet()) {
            // 대분류 항목이 null이 아닌지 확인
            assertNotNull(entry.getKey(), "Major Category should not be null");
            // 중분류 리스트가 null이 아닌지 확인
            assertNotNull(entry.getValue(), "Middle Categories list should not be null");

            // 중분류 리스트가 비어있지 않은지 확인
            assertFalse(entry.getValue().isEmpty(), "Middle Categories list should not be empty");

            // 로그 출력 (선택 사항)
            System.out.println("Major Category: " + entry.getKey().getMajorCategoryName());
            for (MiddleCategoryDto middleCategory : entry.getValue()) {
                System.out.println(" - Middle Category: " + middleCategory.getMiddleCategoryName());
            }
        }
    }






    }




