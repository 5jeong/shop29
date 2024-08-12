package com.toy2.shop29.qna.repository.attachment;

import com.toy2.shop29.qna.domain.AttachmentDto;
import org.apache.ibatis.session.SqlSession;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AttachmentDaoImpl implements AttachmentDao{

    SqlSession session;
    String namespace = "com.toy2.shop29.qna.AttachmentMapper.";

    public AttachmentDaoImpl(SqlSession session){
        this.session = session;
    }


    /*
        CREATE
     */
    @Override
    public int insert(AttachmentDto attachmentDto) throws DataAccessException {
        return session.insert(namespace + "insert", attachmentDto);
    }

    @Override
    public int insertList(List<AttachmentDto> attachmentDtos) throws DataAccessException {
        return session.insert(namespace + "insertList", attachmentDtos);
    }


    /*
        READ
     */
    @Override
    public AttachmentDto select(int attachmentId, Boolean isActive) throws DataAccessException {
        Map<String, Object> map = new HashMap<>();
        map.put("attachmentId", attachmentId);
        map.put("isActive", isActive);

        return session.selectOne(namespace + "select", map);
    }

    @Override
    public List<AttachmentDto> selectAll(Boolean isActive) throws DataAccessException {
        return session.selectList(namespace + "selectAll", isActive);
    }

    @Override
    public int count() throws DataAccessException {
        return session.selectOne(namespace + "count");
    }

    /*
        UPDATE
     */
    @Override
    public int update(AttachmentDto attachmentDto) throws DataAccessException {
        return session.update(namespace + "update", attachmentDto);
    }


    /*
        DELETE
     */

    @Override
    public int deleteAll() throws DataAccessException {
        return session.delete(namespace + "deleteAll");
    }
}
