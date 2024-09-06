package com.toy2.shop29.qna.repository.qna;

import com.toy2.shop29.qna.domain.QnaDto;
import org.apache.ibatis.session.SqlSession;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Repository
public class QnaDaoImpl implements QnaDao{

    SqlSession session;
    String namespace = "com.toy2.shop29.qna.QnaMapper.";

    public QnaDaoImpl(SqlSession session){
        this.session = session;
    }

    /*
        CREATE
     */
    @Override
    public int insert(QnaDto qnaDto) {
        return session.insert(namespace + "insert", qnaDto);
    }


    /*
        READ
    */
    @Override
    public List<QnaDto> selectAllWith(String userId, Integer limit, Integer offset, Boolean isActive) throws DataAccessException {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("limit", limit);
        map.put("offset", offset);
        map.put("isActive", isActive);

        return session.selectList(namespace + "selectAllWith", map);
    }

    @Override
    public List<QnaDto> selectAllWithFilter(int limit, int offset, String qnaTypeId, Boolean isAnswered, Boolean isActive) throws DataAccessException {
        Map<String, Object> map = new HashMap<>();
        map.put("limit", limit);
        map.put("offset", offset);
        map.put("qnaTypeId", qnaTypeId);
        map.put("isAnswered", isAnswered);
        map.put("isActive", isActive);

        return session.selectList(namespace + "selectAllWithFilter", map);
    }

    @Override
    public QnaDto selectWith(int qnaId) throws DataAccessException {
        return session.selectOne(namespace + "selectWith", qnaId);
    }

    @Override
    public QnaDto select(int qnaId, Boolean isDeleted) {
        Map<String, Object> map = new HashMap<>();
        map.put("qnaId", qnaId);
        map.put("isDeleted", isDeleted);

        return session.selectOne(namespace + "select", map);
    }

    @Override
    public int countByUserId(String userId) {
        return session.selectOne(namespace + "countByUserId", userId);
    }

    @Override
    public int countForAdminWithFilter(String qnaTypeId, Boolean isAnswered) {
        Map<String, Object> map = new HashMap<>();
        map.put("qnaTypeId", qnaTypeId);
        map.put("isAnswered", isAnswered);

        return session.selectOne(namespace + "countForAdminWithFilter", map);
    }

    @Override
    public int count() {
        return session.selectOne(namespace + "count");
    }

    /*
        UPDATE
    */
    @Override
    public int update(QnaDto qnaDto) {
        return session.update(namespace + "update", qnaDto);
    }

    /*
        DELETE
    */
    @Override
    public int softDelete(int qnaId, String userId) {
        Map<String, Object> map = new HashMap<>();
        map.put("qnaId", qnaId);
        map.put("userId", userId);

        return session.update(namespace + "softDelete", map);
    }

    @Override
    public int deleteAll() {
        return session.delete(namespace + "deleteAll");
    }
}
