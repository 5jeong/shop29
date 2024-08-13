package com.toy2.shop29.product.dao;

import com.toy2.shop29.product.domain.MiddleCategoryDto;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class MiddleCategoryDao {

    @Autowired
    private SqlSession session;

    String namespace = "com.toy2.shop29.product.dao.MiddleCategoryMapper.";

    // middleCategory Id에 맞는 거 하나 가져오기
    public MiddleCategoryDto selectById(int middleCategoryId) {
        return session.selectOne(namespace + "selectById", middleCategoryId);
    }

    // majorCategoryId와 일치하는 middleCategory 모두 가져오기
    public List<MiddleCategoryDto> selectByMajorCategoryId(int majorCategoryId) {
        return session.selectList(namespace + "selectByMajorCategoryId", majorCategoryId);
    }

    // 새 middleCategory 추가
    public int insert(MiddleCategoryDto middleCategoryDto) {
        return session.insert(namespace + "insert", middleCategoryDto);
    }

    // middleCategory 수정
    public int update(MiddleCategoryDto middleCategoryDto) {
        return session.update(namespace + "update", middleCategoryDto);
    }

    // middleCategory Id에 해당하는 거 삭제
    public int delete(int middleCategoryId) {
        return session.delete(namespace + "delete", middleCategoryId);
    }

    public MiddleCategoryDto selectByNameAndMajorId(String middleCategoryName, int majorCategoryId) {
        Map<String, Object> params = new HashMap<>();
        params.put("middleCategoryName", middleCategoryName);
        params.put("majorCategoryId", majorCategoryId);
        return session.selectOne(namespace + "selectByNameAndMajorId", params);
    }


}
