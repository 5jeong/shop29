package com.toy2.shop29.qna.repository.qnatype;

import com.toy2.shop29.qna.domain.QnaTypeDto;
import org.apache.ibatis.session.SqlSession;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class QnaTypeDaoImpl implements QnaTypeDao {

    private SqlSession session;
    private String namespace = "com.toy2.shop29.qna.QnaTypeMapper.";

    public QnaTypeDaoImpl(SqlSession session){
        this.session = session;
    }

    /*
        CREATE
    */
    @Override
    public int insert(QnaTypeDto qnaTypeDto) throws DataAccessException {
        return session.insert(namespace + "insert", qnaTypeDto);
    }

    /*
        READ
    */
    @Override
    public List<QnaTypeDto> selectAll(boolean withParent, Boolean isActive) throws DataAccessException {
        Map<String,Object> map = new HashMap<>();
        map.put("withParent", withParent);
        map.put("isActive", isActive);

        return session.selectList(namespace + "selectAll", map);
    }

    @Override
    public QnaTypeDto select(String qnaTypeId, boolean withParent, Boolean isActive) throws DataAccessException {
        Map<String,Object> map = new HashMap<>();
        map.put("qnaTypeId", qnaTypeId);
        map.put("withParent", withParent);
        map.put("isActive", isActive);

        return session.selectOne(namespace + "select", map);
    }

    @Override
    public int count() {
        return session.selectOne(namespace + "count");
    }

    /*
        UPDATE
    */
    @Override
    public int update(QnaTypeDto qnaTypeDto) {
        return session.update(namespace + "update", qnaTypeDto);
    }

    /*
        DELETE
    */
    @Override
    public int delete(String qnaTypeId) {
        return session.delete(namespace + "delete", qnaTypeId);
    }

    @Override
    public int deleteAll() throws DataAccessException {
        return session.delete(namespace + "deleteAll");
    }
}
