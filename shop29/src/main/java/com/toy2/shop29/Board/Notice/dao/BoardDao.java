package com.toy2.shop29.Board.Notice.dao;

import com.toy2.shop29.Board.Notice.domain.BoardDto;

import java.util.List;
import java.util.Map;

public interface BoardDao {
    BoardDto select(int notice_number);

    BoardDto select(Integer notice_number);
    int delete(Integer notice_number, String notice_creatior);
    int insert(BoardDto boardDto);
    int update(BoardDto boardDto);

    List<BoardDto> selectAll();
    List<BoardDto> selectPage(Map map);
    int deleteAll();
    int count();

}
