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
BoardDao boardDao;

    @Override
    public int getCount() {
        return boardDao.count();
    }

    @Override
    @Transactional
    public int remove(Integer noticeId, String noticeCreatorId) {
        return boardDao.delete(noticeId, noticeCreatorId);
    }

    @Override
    @Transactional
    public int write(BoardDto boardDto) {
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
    public List<BoardDto> getPage(Map map) {
        return boardDao.selectPage(map);
    }

    @Override
    @Transactional
    public int modify(BoardDto boardDto) {
        return boardDao.update(boardDto);
    }
}
