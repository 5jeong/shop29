package com.toy2.shop29.Board.Notice.dao;

import com.toy2.shop29.Board.Notice.domain.BoardDto;

import java.util.List;
import java.util.Map;

public interface BoardDao {

    // 특정 공지사항을 조회하는 메서드
    BoardDto select(Integer noticeId);

    // 공지사항을 삭제하는 메서드
    int delete(Integer noticeId, String noticeCreatorId);

    // 공지사항을 삽입하는 메서드
    int insert(BoardDto boardDto);

    // 공지사항을 업데이트하는 메서드
    int update(BoardDto boardDto);

    // 모든 공지사항을 조회하는 메서드
    List<BoardDto> selectAll();

    // 페이지네이션을 적용하여 공지사항을 조회하는 메서드
    List<BoardDto> selectPage(Map<String, Object> map);

    // 모든 공지사항을 삭제하는 메서드
    int deleteAll();

    // 공지사항의 총 개수를 조회하는 메서드
    int count();

    // 상단 고정 공지사항 목록을 가져오는 메서드
    List<BoardDto> selectFixedNotices();
}
