package com.toy2.shop29.product.service;

import com.toy2.shop29.product.dao.CategoryDao;
import com.toy2.shop29.product.domain.CategoryDto;
import com.toy2.shop29.product.domain.MajorCategoryDto;
import com.toy2.shop29.product.domain.MiddleCategoryDto;
import com.toy2.shop29.product.domain.SmallCategoryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryDao categoryDao;

    //모든 대분류 카테고리 리스트 가져오는 메서드
    public List<MajorCategoryDto> getMajorCategories(){
        return categoryDao.findAllMajorCategories();
    }

    //특정 대분류에 속하는 중분류 카테고리 리스트를 가져오는 메서드
    public List<MiddleCategoryDto> getMiddleCategoriesByMajorId(int majorId){
        return categoryDao.findMiddleCategoriesByMajorId(majorId);
    }

    //특정 중분류에 속하는 소분류 카테고리 리스트를 가져오는 메서드
    public List<SmallCategoryDto> getSmallCategoriesByMiddleId(int middleId){
        return categoryDao.findSmallCategoriesByMiddleId(middleId);
    }

    //모든 카테고리 다 가져오는 메서드
    public List<CategoryDto> getAllCategories(){
        return categoryDao.findAllCategories();
    }




}
