package com.toy2.shop29.product.dao.category;

import com.toy2.shop29.product.domain.category.MajorCategoryDto;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MajorCategoryDao {

    @Autowired
    private SqlSession session;

    String namespace = "com.toy2.shop29.product.dao.MajorCategoryMapper.";

    // 모든 행 정보 가져오기
    public List<MajorCategoryDto> selectAll() {
        return session.selectList(namespace + "selectAll");
    }

    // 특정 id 행 정보 가져오기
    public MajorCategoryDto selectById(int majorCategoryId) {
        return session.selectOne(namespace + "selectById", majorCategoryId);
    }

    //새로운 major category 추가
    public int insert(MajorCategoryDto majorCategoryDto) {
        return session.insert(namespace + "insert", majorCategoryDto);
    }

    //특정 id 행 정보 없애기
    public int delete(int majorCategoryId) {
        return session.delete(namespace + "delete", majorCategoryId);
    }

    public MajorCategoryDto selectByName(String majorCategoryName) {
        return session.selectOne(namespace + "selectByName", majorCategoryName);
    }




}
