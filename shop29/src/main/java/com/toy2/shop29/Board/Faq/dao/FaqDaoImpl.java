package com.toy2.shop29.Board.Faq.dao;

import com.toy2.shop29.Board.Faq.domain.FaqDto;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class FaqDaoImpl implements FaqDao {

    @Autowired
    private SqlSession session;

    private static final String namespace = "com.toy2.shop29.Board.Faq.dao.FaqMapper.";

    @Override
    public FaqDto select(Integer faqId) {
        return session.selectOne(namespace + "select", faqId);
    }

    @Override
    public int delete(Integer faqId, String faqCreatorId) {
        return session.delete(namespace + "delete", Map.of("faqId", faqId, "faqCreatorId", faqCreatorId));
    }

    @Override
    public int insert(FaqDto faqDto) {
        return session.insert(namespace + "insert", faqDto);
    }

    @Override
    public int update(FaqDto faqDto) {
        return session.update(namespace + "update", faqDto);
    }

    @Override
    public List<FaqDto> selectAll() {
        return session.selectList(namespace + "selectAll");
    }

    @Override
    public List<FaqDto> selectPage(Map<String, Object> map) {
        return session.selectList(namespace + "selectPage", map);
    }

    @Override
    public int deleteAll() {
        return session.delete(namespace + "deleteAll");
    }

    @Override
    public int count() {
        return session.selectOne(namespace + "count");
    }

    @Override
    public int countBySearchQuery(Map<String, Object> params) {
        return session.selectOne(namespace + "countBySearchQuery", params);
    }

    @Override
    public List<FaqDto> selectPageWithSearch(Map<String, Object> map) {
        return session.selectList(namespace + "selectPageWithSearch", map);
    }
}
