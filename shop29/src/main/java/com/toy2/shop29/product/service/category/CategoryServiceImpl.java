package com.toy2.shop29.product.service.category;

import com.toy2.shop29.product.dao.category.CategoryDao;

import com.toy2.shop29.product.domain.category.MajorCategoryDto;
import com.toy2.shop29.product.domain.category.MajorMiddleDto;
import com.toy2.shop29.product.domain.category.MiddleCategoryDto;
import com.toy2.shop29.product.domain.category.SmallCategoryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryDao categoryDao;

    @Override
    public List<MajorCategoryDto> getAllMajorCategories(){
        return categoryDao.findAllMajorCategories();
    }

    @Override
    public List<MajorMiddleDto> getMiddleByMajor(int majorId){
        return categoryDao.getMiddleByMajor(majorId);
    }

    @Override
    public List<SmallCategoryDto> getSmallCategoriesByMiddle(int middleCategoryId) {
        return categoryDao.getSmallCategoriesByMiddle(middleCategoryId);
    }

    @Override
    public List<MiddleCategoryDto> getRelatedMiddleCategories(int middleCategoryId) {
        return categoryDao.getRelatedMiddleCategories(middleCategoryId);
    }

    @Override
    public MiddleCategoryDto getMiddleCategoryBySmall(int smallCategoryId) {
        return categoryDao.getMiddleCategoryBySmall(smallCategoryId);
    }








}
