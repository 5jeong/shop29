package com.toy2.shop29.product.dao.category;

import com.toy2.shop29.product.domain.category.*;

import java.util.List;

public interface CategoryDao {

    //소분류Id로 중분류Dto 반환
    MiddleCategoryDto selectMiddleBySmall(int smallCategoryId);

    //상품리스트페이지에서 사용
    //중분류Id에 따른 소분류 리스트 반환
    List<SmallCategoryDto> selectSmallsByMiddle(int middleCategoryId);

    //같은 대분류를 가지는 중분류 리스트 반환
    List<MiddleCategoryDto> selectRelatedMiddles(int middleCategoryId);

    //메인페이지에서 사용
    //모든 대분류 리스트 반환
    List<MajorCategoryDto> selectAllMajors();

    //대분류Id에 따른 중분류 리스트 반환
    List<MajorMiddleDto> selectMiddleMajor(int majorCategoryId);

}