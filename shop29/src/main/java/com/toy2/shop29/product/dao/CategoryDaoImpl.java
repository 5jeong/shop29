package com.toy2.shop29.product.dao;

import com.toy2.shop29.product.domain.CategoryDto;
import com.toy2.shop29.product.domain.MajorCategoryDto;
import com.toy2.shop29.product.domain.MiddleCategoryDto;
import com.toy2.shop29.product.domain.SmallCategoryDto;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CategoryDaoImpl implements CategoryDao {


    @Autowired
    private SqlSession session;

    String namespace = "com.toy2.shop29.product.dao.CategoryMapper.";


    //모든 Major카테고리들 List로 가져오기
    @Override
    public List<MajorCategoryDto> findAllMajorCategories(){
        return session.selectList(namespace+"findAllMajorCategories");
    }

    //특정 MajorId에 해당하는 모든 Middle카테고리를 List로 가져오기
    @Override
    public List<MiddleCategoryDto> findMiddleCategoriesByMajorId(int majorCategoryId) {
        return session.selectList(namespace + "findMiddleCategoriesByMajorId", majorCategoryId);
    }

    //특정 MiddleId에 해당하는 모든 Small카테고리를 List로 가져오기
    @Override
    public List<SmallCategoryDto> findSmallCategoriesByMiddleId(int middleCategoryId) {
        return session.selectList(namespace + "findSmallCategoriesByMiddleId", middleCategoryId);
    }

    // 대분류, 중분류, 소분류 상관없이 모든 카테고리 가져오기
    @Override
    public List<CategoryDto> findAllCategories() {
        return session.selectList(namespace + "findAllCategories");
    }


}
