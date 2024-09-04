package com.toy2.shop29.product.dao.category;

import com.toy2.shop29.product.domain.category.MajorCategoryDto;
import com.toy2.shop29.product.domain.category.MajorMiddleDto;
import com.toy2.shop29.product.domain.category.MiddleCategoryDto;
import com.toy2.shop29.product.domain.category.SmallCategoryDto;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CategoryDaoImpl implements CategoryDao {


    @Autowired
    private SqlSession session;

    String namespace = "com.toy2.shop29.product.dao.CategoryMapper.";


    @Override
    public MiddleCategoryDto selectMiddleBySmall(int smallCategoryId){
        return session.selectOne(namespace+ "selectMiddleBySmall", smallCategoryId);
    }

    @Override
    public List<SmallCategoryDto> selectSmallsByMiddle(int middleCategoryId) {
        return session.selectList(namespace+ "selectSmallsByMiddle", middleCategoryId);
    }

    //같은 대분류를 가지는 중분류 리스트 반환
    @Override
    public List<MiddleCategoryDto> selectRelatedMiddles(int middleCategoryId) {
        return session.selectList(namespace+ "selectRelatedMiddles", middleCategoryId);
    }

    //메인페이지에서 사용
    //모든 Major카테고리들 List로 가져오기
    @Override
    public List<MajorCategoryDto> selectAllMajors(){
        return session.selectList(namespace+"selectAllMajors");
    }

    //MajorId에 해당하는 middle 가져오기
    public List<MajorMiddleDto> selectMiddleMajor(int majorCategoryId) {
        return session.selectList(namespace+"selectMiddleMajor", majorCategoryId);
    }

}










