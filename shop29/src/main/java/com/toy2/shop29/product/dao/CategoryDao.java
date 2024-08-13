package com.toy2.shop29.product.dao;

import com.toy2.shop29.product.domain.CategoryDto;
import com.toy2.shop29.product.domain.MajorCategoryDto;
import com.toy2.shop29.product.domain.MiddleCategoryDto;
import com.toy2.shop29.product.domain.SmallCategoryDto;

import java.util.List;

public interface CategoryDao {
    //모든 Major카테고리들 List로 가져오기
    List<MajorCategoryDto> findAllMajorCategories();

    //특정 MajorId에 해당하는 모든 Middle카테고리를 List로 가져오기
    List<MiddleCategoryDto> findMiddleCategoriesByMajorId(int majorCategoryId);

    //특정 MiddleId에 해당하는 모든 Small카테고리를 List로 가져오기
    List<SmallCategoryDto> findSmallCategoriesByMiddleId(int middleCategoryId);

    // 대분류, 중분류, 소분류 상관없이 모든 카테고리 가져오기
    List<CategoryDto> findAllCategories();
}
