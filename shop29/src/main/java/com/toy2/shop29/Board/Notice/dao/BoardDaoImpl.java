package com.toy2.shop29.Board.Notice.dao;

import com.toy2.shop29.Board.Notice.domain.BoardDto;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class BoardDaoImpl implements BoardDao {

    @Autowired
    private SqlSession session;

    private static final String namespace = "com.toy2.shop29.Board.Notice.dao.BoardMapper.";

    @Override
    public BoardDto select(Integer noticeId) {
        return session.selectOne(namespace + "select", noticeId);
    }

    @Override
    public int delete(Integer noticeId, String noticeCreatorId) {
        // MyBatis의 delete 쿼리 실행
        return session.delete(namespace + "delete", Map.of("noticeId", noticeId, "noticeCreatorId", noticeCreatorId));
    }

    @Override
    public int insert(BoardDto boardDto) {
        // MyBatis의 insert 쿼리 실행
        return session.insert(namespace + "insert", boardDto);
    }

    @Override
    public int update(BoardDto boardDto) {
        // MyBatis의 update 쿼리 실행
        return session.update(namespace + "update", boardDto);
    }

    @Override
    public List<BoardDto> selectAll() {
        // MyBatis의 selectAll 쿼리 실행
        return session.selectList(namespace + "selectAll");
    }

    @Override
    public List<BoardDto> selectPage(Map<String, Object> map) {
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
    public List<BoardDto> selectFixedNotices() {
        return session.selectList(namespace + "selectFixedNotices");
    }
    @Override
    public int updateFixedNoticePriority(Integer noticeId, int priority) {
        Map<String, Object> params = new HashMap<>();
        params.put("noticeId", noticeId);
        params.put("priority", priority);
        return session.update(namespace + "updateFixedNoticePriority", params);
    }

}
