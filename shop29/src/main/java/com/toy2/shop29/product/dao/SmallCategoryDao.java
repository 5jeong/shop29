package com.toy2.shop29.product.dao;

import com.toy2.shop29.product.domain.SmallCategoryDto;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SmallCategoryDao {

    @Autowired
    private SqlSession session;

    String namespace = "com.toy2.shop29.product.dao.SmallCategoryMapper.";

    // 모든 smallCategory 가져오기
    public List<SmallCategoryDto> selectAll() {
        return session.selectList(namespace + "selectAll");
    }

    // smallCategory id에 해당하는 하나 가져오기
    public SmallCategoryDto selectById(int smallCategoryId) {
        return session.selectOne(namespace + "selectById", smallCategoryId);
    }

    // middleCategory id에 해당하는 모든 smallCategory 가져오기
    public List<SmallCategoryDto> selectByMiddleCategoryId(int middleCategoryId) {
        return session.selectList(namespace + "selectByMiddleCategoryId", middleCategoryId);
    }

    // smallCategory 하나 추가
    public int insert(SmallCategoryDto smallCategoryDto) {
        return session.insert(namespace + "insert", smallCategoryDto);
    }

    // smallCetegory 수정
    public int update(SmallCategoryDto smallCategoryDto) {
        return session.update(namespace + "update", smallCategoryDto);
    }

    // SmallCategory id 맞는거 하나 삭제
    public int delete(int smallCategoryId) {
        return session.delete(namespace + "delete", smallCategoryId);
    }


}
