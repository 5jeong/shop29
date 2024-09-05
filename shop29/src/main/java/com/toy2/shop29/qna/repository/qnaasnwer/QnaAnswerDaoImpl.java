package com.toy2.shop29.qna.repository.qnaasnwer;

import com.toy2.shop29.qna.domain.QnaAnswerDto;
import org.apache.ibatis.session.SqlSession;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class QnaAnswerDaoImpl implements QnaAnswerDao{

    SqlSession session;
    String namespace = "com.toy2.shop29.qna.QnaAnswerMapper.";

    public QnaAnswerDaoImpl(SqlSession session){
        this.session = session;
    }

    /*
        CREATE
     */
    @Override
    public int insert(QnaAnswerDto qnaAnswerDto) throws DataAccessException {
        return session.insert(namespace + "insert", qnaAnswerDto);
    }




    /*
        READ
     */
    @Override
    public QnaAnswerDto selectBy(int qnaId, Boolean isDeleted) throws DataAccessException {
        Map<String, Object> map = new HashMap<>();
        map.put("qnaId", qnaId);
        map.put("isDeleted", isDeleted);

        return session.selectOne(namespace + "selectBy", map);
    }

    @Override
    public QnaAnswerDto select(int qnaAnswerId, Boolean isDeleted) throws DataAccessException {
        Map<String, Object> map = new HashMap<>();
        map.put("qnaAnswerId", qnaAnswerId);
        map.put("isDeleted", isDeleted);

        return session.selectOne(namespace + "select", map);
    }

    @Override
    public List<QnaAnswerDto> selectAll(Boolean isDeleted) throws DataAccessException {
        return session.selectList(namespace + "selectAll", isDeleted);
    }

    @Override
    public int count(){
        return session.selectOne(namespace + "count");
    }


    /*
        UPDATE
     */
    @Override
    public int update(QnaAnswerDto qnaAnswerDto) throws DataAccessException {
        return session.update(namespace + "update", qnaAnswerDto);
    }


    /*
        DELETE
     */
    @Override
    public int softDelete(int qnaAnswerId, String userId) throws DataAccessException {
        Map<String, Object> map = new HashMap<>();
        map.put("qnaAnswerId", qnaAnswerId);
        map.put("deletedId", userId);

        return session.update(namespace + "softDelete", map);
    }

    @Override
    public int deleteAll() throws DataAccessException {
        return session.delete(namespace + "deleteAll");
    }
}
