package com.toy2.shop29.product.dao;

import com.toy2.shop29.product.domain.*;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CategoryDao {

//    //select
//    CategoryDto selectCategoryByCategoryDto( CategoryDto categoryDto);

    //select 2
    CategoryDto selectCategorybyId(int majorCategoryId, int middleCategoryId, int smallCategoryId);

//    // insert
//    int insertCategory(CategoryDto categoryDto);

//    //update
//    int updateCategory(CategoryDto categoryDto);

//    //delete by Id
//    int deleteCategory(int majorCategoryId, int middleCategoryId, int smallCategoryId);

//    //delete - All
//    int deleteAll();

//    //count
//    int countCategories();


    int insertMajorCategory(MajorCategoryDto majorCategoryDto);

    int insertMiddleCategory(MiddleCategoryDto middleCategoryDto);

    int insertSmallCategory(SmallCategoryDto smallCategoryDto);


    int updateMajorCategory(MajorCategoryDto majorCategoryDto);

    int updateMiddleCategory(MiddleCategoryDto middleCategoryDto);

    int updateSmallCategory(SmallCategoryDto smallCategoryDto);


    int deleteMajorCategory(int majorCategoryId);

    int deleteMiddleCategory(int middleCategoryId);

    int deleteSmallCategory(int smallCategoryId);


    int deleteAllCategories();


    List<CategoryDto> findAllCategories();


    //모든 Major카테고리들 List로 가져오기
    List<MajorCategoryDto> findAllMajorCategories();

    //
//    //특정 MajorId에 해당하는 모든 Middle카테고리를 List로 가져오기
//    List<MiddleCategoryDto> findMiddleCategoriesByMajorId(int majorCategoryId);
//
//    //특정 MiddleId에 해당하는 모든 Small카테고리를 List로 가져오기
//    List<SmallCategoryDto> findSmallCategoriesByMiddleId(int middleCategoryId);
//
//    // 대분류, 중분류, 소분류 상관없이 모든 카테고리 가져오기
//    List<CategoryDto> findAllCategories();
//
//    //소분류에 해당하는 모든 제품 가져오기
//    List<ProductDto> findProductsBySmallId(int smallCategoryId);
//
    //대분류 중분류 JOIN해서 selsect
    List<CategoryDto> findMajorMiddle();

    //중분류만 가져오기
    List<MiddleCategoryDto> findAllMiddleCategories();

    int countMiddleCategory(int middleCategoryId);

    MiddleCategoryDto findMiddleCategoryById(int middleCategoryId);

    List<MiddleCategoryDto> findMiddleCategoriesByMajorCategoryId(int majorCategoryId);

}