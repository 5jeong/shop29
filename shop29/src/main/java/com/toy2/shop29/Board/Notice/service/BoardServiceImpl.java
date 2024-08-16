package com.toy2.shop29.Board.Notice.service;

import com.toy2.shop29.Board.Notice.dao.BoardDao;
import com.toy2.shop29.Board.Notice.domain.BoardDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class BoardServiceImpl implements BoardService {

    @Autowired
    private BoardDao boardDao;

    @Override
    public int getCount() {
        return boardDao.count();
    }

    @Override
    @Transactional
    public int remove(Integer noticeId, String noticeCreatorId) {
        int result = boardDao.delete(noticeId, noticeCreatorId);
        return boardDao.delete(noticeId, noticeCreatorId);
    }

    @Override
    @Transactional
    public int write(BoardDto boardDto) {
        // 게시물 삽입
        return boardDao.insert(boardDto);
    }

    @Override
    public List<BoardDto> getList() {
        return boardDao.selectAll();
    }

    @Override
    public BoardDto read(Integer noticeId) {
        return boardDao.select(noticeId);
    }

    @Override
    public List<BoardDto> getPage(Map<String, Object> map) {
        // 페이지 처리를 위한 게시물 목록 가져오기
        // 여기서 notice_number 만들어서 list 값에 다시 할당
        // offset, pageSize, pageNo
        return boardDao.selectPage(map);
    }

    @Override
    @Transactional
    public int modify(BoardDto boardDto) {
        return boardDao.update(boardDto);
    }
    // 상단 고정된 공지사항을 가져오는 메서드 추가
    @Override
    public List<BoardDto> getFixedNotices() {
        return boardDao.selectFixedNotices();
    }


}
