package com.toy2.shop29.Board.Notice.service;

import com.toy2.shop29.Board.Notice.domain.BoardDto;

import java.util.List;
import java.util.Map;

public interface BoardService {
    int getCount() ;

    int remove(Integer noticeId, String noticeCreatorId);

    int write(BoardDto boardDto);

    List<BoardDto> getList();

    BoardDto read(Integer noticeId);

    List<BoardDto> getPage(Map<String, Object> map);

    int modify(BoardDto boardDto);
    // 상단 고정 공지사항 목록을 가져오는 메서드
    List<BoardDto> getFixedNotices();
}
