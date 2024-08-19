package com.toy2.shop29.product.dao.category;

import com.toy2.shop29.product.domain.category.*;

import java.util.List;

public interface CategoryDao {


    //메인페이지에서 사용
    //모든 대분류 리스트 반환
    List<MajorCategoryDto> findAllMajorCategories();

    //대분류Id에 따른 중분류 리스트 반환
    List<MajorMiddleDto> getMiddleByMajor(int majorCategoryId);


    //상품리스트페이지에서 사용
    //중분류Id에 따른 소분류 리스트 반환
    List<SmallCategoryDto> getSmallCategoriesByMiddle(int middleCategoryId);

    //같은 대분류를 가지는 중분류 리스트 반환
    List<MiddleCategoryDto> getRelatedMiddleCategories(int middleCategoryId);

    //소분류Id로 중분류Dto 반환
    MiddleCategoryDto getMiddleCategoryBySmall(int smallCategoryId);







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