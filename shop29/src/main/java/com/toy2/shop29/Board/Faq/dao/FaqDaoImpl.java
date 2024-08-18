package com.toy2.shop29.Board.Faq.dao;

import com.toy2.shop29.Board.Faq.domain.FaqDto;
import com.toy2.shop29.Board.Notice.domain.BoardDto;
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
        // MyBatis의 delete 쿼리 실행
        return session.delete(namespace + "delete", Map.of("faqId", faqId, "faqCreatorId", faqCreatorId));
    }

    @Override
    public int insert(FaqDto faqDto) {
        // MyBatis의 insert 쿼리 실행
        return session.insert(namespace + "insert", faqDto);
    }

    @Override
    public int update(FaqDto faqDto) {
        // MyBatis의 update 쿼리 실행
        return session.update(namespace + "update", faqDto);
    }

    @Override
    public List<FaqDto> selectAll() {
        // MyBatis의 selectAll 쿼리 실행
        return session.selectList(namespace + "selectAll");
    }

    @Override
    public List<FaqDto> selectPage(Map<String, Object> map) {
        // MyBatis의 selectPage 쿼리 실행
        return session.selectList(namespace + "selectPage", map);
    }

    @Override
    public int deleteAll() {
        // MyBatis의 deleteAll 쿼리 실행
        return session.delete(namespace + "deleteAll");
    }

    @Override
    public int count() {
        // MyBatis의 count 쿼리 실행
        return session.selectOne(namespace + "count");
    }
    @Override
    public int countBySearchQuery(String searchQuery) {
        return session.selectOne(namespace + "countBySearchQuery", searchQuery);
    }

    @Override
    public List<FaqDto> selectPageWithSearchQuery(Map<String, Object> map) {
        return session.selectList(namespace + "selectPageWithSearchQuery", map);
    }
}

