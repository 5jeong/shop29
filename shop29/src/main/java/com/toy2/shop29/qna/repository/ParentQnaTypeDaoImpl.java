package com.toy2.shop29.qna.repository;

import com.toy2.shop29.qna.dto.ParentQnaTypeDto;
import org.apache.ibatis.session.SqlSession;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ParentQnaTypeDaoImpl implements ParentQnaTypeDao{

    private SqlSession session;
    private String namespace = "com.toy2.shop29.qna.ParentQnaTypeMapper.";

    public ParentQnaTypeDaoImpl(SqlSession session){
        this.session = session;
    }

    /*
        CREATE
     */
    @Override
    public int insert(ParentQnaTypeDto parentQnaTypeDto) throws DataAccessException {
        return session.insert(namespace + "insert", parentQnaTypeDto);
    }

    /*
        READ
     */
    @Override
    public ParentQnaTypeDto select(String parentQnaTypeId) throws DataAccessException {
        return session.selectOne(namespace + "select", parentQnaTypeId);
    }

    @Override
    public List<ParentQnaTypeDto> selectAll() throws DataAccessException{
        return session.selectList(namespace + "selectAll");
    }

    @Override
    public int count() throws DataAccessException{
        return session.selectOne(namespace + "count");
    }

    /*
        UPDATE
     */
    @Override
    public int update(ParentQnaTypeDto parentQnaTypeDto) throws DataAccessException{
        return session.update(namespace + "update", parentQnaTypeDto);
    }

    /*
        DELETE
     */
    @Override
    public int delete(String parentQnaTypeId) throws DataAccessException{
        return session.delete(namespace + "delete", parentQnaTypeId);
    }

    @Override
    public int deleteAll() throws DataAccessException{
        return session.delete(namespace + "deleteAll");
    }
}
