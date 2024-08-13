package com.toy2.shop29.product.dao;

import com.toy2.shop29.product.domain.OptionValueDto;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OptionValueDao {

    @Autowired
    private SqlSession session;

    private static final String namespace = "com.toy2.shop29.product.dao.OptionValueMapper.";

    public List<OptionValueDto> selectAll() {
        return session.selectList(namespace + "selectAll");
    }

    public OptionValueDto selectById(int optionValueId) {
        return session.selectOne(namespace + "selectById", optionValueId);
    }

    public List<OptionValueDto> selectByOptionKeyId(int optionKeyId) {
        return session.selectList(namespace + "selectByOptionKeyId", optionKeyId);
    }

    public int insert(OptionValueDto optionValueDto) {
        return session.insert(namespace + "insert", optionValueDto);
    }

    public int update(OptionValueDto optionValueDto) {
        return session.update(namespace + "update", optionValueDto);
    }

    public int delete(int optionValueId) {
        return session.delete(namespace + "delete", optionValueId);
    }
}