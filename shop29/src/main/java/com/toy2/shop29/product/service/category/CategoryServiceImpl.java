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
    public MiddleCategoryDto findMiddleBySmall(int smallCategoryId) {
        return categoryDao.selectMiddleBySmall(smallCategoryId);
    }

    @Override
    public List<SmallCategoryDto> findSmallsByMiddle(int middleCategoryId) {
        return categoryDao.selectSmallsByMiddle(middleCategoryId);
    }

    @Override
    public List<MiddleCategoryDto> findRelatedMiddles(int middleCategoryId) {
        return categoryDao.selectRelatedMiddles(middleCategoryId);
    }

    @Override
    public List<MajorCategoryDto> findAllMajors(){
        return categoryDao.selectAllMajors();
    }

    @Override
    public List<MajorMiddleDto> findMiddleMajor(int majorId){
        return categoryDao.selectMiddleMajor(majorId);
    }

}
