package com.toy2.shop29.product.service.category;

import com.toy2.shop29.product.domain.category.MajorCategoryDto;
import com.toy2.shop29.product.domain.category.MajorMiddleDto;
import com.toy2.shop29.product.domain.category.MiddleCategoryDto;
import com.toy2.shop29.product.domain.category.SmallCategoryDto;

import java.util.List;

public interface CategoryService {

    //소분류Id로 중분류Id 가져오는 메서드
    MiddleCategoryDto findMiddleBySmall(int smallCategoryId);

    //2. 상품리스트페이지에 사용
    //2-1.기존 중분류의 소분류를 가져오는 메서드
    List<SmallCategoryDto> findSmallsByMiddle(int middleCategoryId);

    //2-2. 특정 중분류와 같은 대분류를 가지는 다른 중분류 리스트를 가져오는 메서드
    List<MiddleCategoryDto> findRelatedMiddles(int middleCategoryId);

    //1. 메인페이지에 사용
    //1-1. 모든 대분류 카테고리 리스트 가져오는 메서드
    List<MajorCategoryDto> findAllMajors();

    //1-2. 모든 대분류와 그에 맞는 중분류 리스트를 가져오는 메서드
    List<MajorMiddleDto> findMiddleMajor(int majorId);

}
