package com.toy2.shop29.qna.repository.parentqnatype;

import com.toy2.shop29.qna.domain.ParentQnaTypeDto;
import org.apache.ibatis.session.SqlSession;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ParentQnaTypeDaoImpl implements ParentQnaTypeDao {

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
    public ParentQnaTypeDto select(String parentQnaTypeId, Boolean isActive) throws DataAccessException {
        Map<String, Object> map = new HashMap<>();
        map.put("parentQnaTypeId", parentQnaTypeId);
        map.put("isActive", isActive);

        return session.selectOne(namespace + "select", map);
    }

    @Override
    public List<ParentQnaTypeDto> selectAll(Boolean isActive) throws DataAccessException{
        return session.selectList(namespace + "selectAll", isActive);
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
