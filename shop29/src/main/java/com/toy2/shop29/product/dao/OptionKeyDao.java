package com.toy2.shop29.product.dao;

import com.toy2.shop29.product.domain.OptionKeyDto;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OptionKeyDao {

    @Autowired
    private SqlSession session;

    String namespace = "com.toy2.shop29.product.dao.OptionKeyMapper.";

    public List<OptionKeyDto> selectAll() {
        return session.selectList(namespace + "selectAll");
    }

    public OptionKeyDto selectById(int optionKeyId) {
        return session.selectOne(namespace + "selectById", optionKeyId);
    }

    public int insert(OptionKeyDto optionKeyDto) {
        return session.insert(namespace + "insert", optionKeyDto);
    }

    public int update(OptionKeyDto optionKeyDto) {
        return session.update(namespace + "update", optionKeyDto);
    }

    public int delete(int optionKeyId) {
        return session.delete(namespace + "delete", optionKeyId);
    }
}






