package com.toy2.shop29.product.service;

import com.toy2.shop29.product.dao.CategoryDao;
import com.toy2.shop29.product.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryDao categoryDao;




    @Override
    public Map<MajorCategoryDto, List<MiddleCategoryDto>> getMajorToMiddleCategoryMap() {
        List<MajorCategoryDto> majorCategories = categoryDao.findAllMajorCategories();
        List<MiddleCategoryDto> middleCategories = categoryDao.findAllMiddleCategories();

        // 결과를 저장할 Map 생성
        Map<MajorCategoryDto, List<MiddleCategoryDto>> majorToMiddleMap = new HashMap<>();

        // 각 대분류에 대해 중분류 리스트를 생성하여 매핑
        for (MajorCategoryDto majorCategory : majorCategories) {
            List<MiddleCategoryDto> associatedMiddleCategories = new ArrayList<>();

            //중분류테이블의 대분류ID아 대분류테이블의 대분류ID를 매칭해서 맞는 것들만 ArrayList에 저장(몇개있는지 모르니까)
            for (MiddleCategoryDto middleCategory : middleCategories) {
                if (middleCategory.getMajorCategoryId() == majorCategory.getMajorCategoryId()) {
                    associatedMiddleCategories.add(middleCategory);
                }
            }

            // 대분류와 그에 해당하는 중분류 리스트를 Map에 추가
            majorToMiddleMap.put(majorCategory, associatedMiddleCategories);
        }

//        // 데이터 로깅
//        System.out.println("majorToMiddleMap = " + majorToMiddleMap);


        return majorToMiddleMap;
    }







    //모든 대분류 카테고리 리스트 가져오는 메서드
    @Override
    public List<MajorCategoryDto> getAllMajorCategories(){
        return categoryDao.findAllMajorCategories();
    }


    @Override
    public List<CategoryDto> getAllCategories() {
        return categoryDao.findAllCategories();
    }


    //모든 대분류와 그에 맞는 중분류 리스트를 가져오는 메서드
    @Override
    public List<CategoryDto> getMajorMiddle(){
        return categoryDao.findMajorMiddle();
    }

//
//    //특정 대분류에 속하는 중분류 카테고리 리스트를 가져오는 메서드
//    @Override
//    public List<MiddleCategoryDto> getAllMiddleCategoriesByMajorId(int majorId){
//        return categoryDao.findMiddleCategoriesByMajorId(majorId);
//    }
//
//    //특정 중분류에 속하는 소분류 카테고리 리스트를 가져오는 메서드
//    @Override
//    public List<SmallCategoryDto> getAllSmallCategoriesByMiddleId(int middleId){
//        return categoryDao.findSmallCategoriesByMiddleId(middleId);
//    }
//
//    @Override
//    public List<CategoryDto> getAllCategories() {
//        return List.of();
//    }
//
//    @Override
//    public List<ProductDto> getProductsBySmallId(int smallId) {
//        return List.of();
//    }

//    //모든 카테고리 다 가져오는 메서드
//    @Override
//    public List<CategoryDto> getAllCategories(){
//        return categoryDao.findAllCategories();
//    }

//    //소분류카테고리에 해당하는 모든 제품 가져오는 메서드
//    public List<ProductDto> getProductsBySmallId(int smallCategoryId) {
//        return categoryDao.findProductsBySmallId(smallCategoryId);
//    }


    @Override
    public List<MiddleCategoryDto> getAllMiddleCategories() {
        return categoryDao.findAllMiddleCategories();
    }

    @Override
    public List<MiddleCategoryDto> getRelatedMiddleCategories(int middleCategoryId) {
        // 선택한 중분류의 대분류 ID 가져오기
        MiddleCategoryDto selectedMiddleCategory = categoryDao.findMiddleCategoryById(middleCategoryId);
        int majorCategoryId = selectedMiddleCategory.getMajorCategoryId();

        // 해당 대분류에 속하는 모든 중분류 가져오기
        return categoryDao.findMiddleCategoriesByMajorCategoryId(majorCategoryId);
    }




}
