package com.toy2.shop29.Board.Notice.dao;

import com.toy2.shop29.Board.Notice.domain.BoardDto;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class BoardDaoImpl implements BoardDao {

    @Autowired
    SqlSession session;

   String namespace="com.toy2.shop29.Board.Notice.dao.BoardMapper.";

    @Override
    public BoardDto select(int notice_number) {
        return null;
    }

    @Override
    public BoardDto select(Integer notice_number) {
        return session.selectOne(namespace +"select", notice_number);
    }


    @Override
    public int delete(Integer notice_number, String notice_creatior) {
        return 0;
    }

    @Override
    public int insert(BoardDto boardDto) {
        return 0;
    }

    @Override
    public int update(BoardDto boardDto) {
        return 0;
    }

    @Override
    public List<BoardDto> selectAll() {
        return List.of();
    }

    @Override
    public List<BoardDto> selectPage(Map map) {
        return List.of();
    }

    @Override
    public int deleteAll() {
        return 0;
    }

    @Override
    public int count() {
        return 0;
    }
}
