package com.toy2.shop29.Board.Notice.dao;

import com.toy2.shop29.Board.Notice.domain.BoardDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
public class BoardDaoImplTest {

    @Autowired
    private BoardDao boardDao;

    @Test
    public void countTest_1() throws Exception {
        boardDao.deleteAll();
        assertTrue(boardDao.count() == 0);

        BoardDto boardDto = new BoardDto("no title", "no content", "asdf","240813");
        assertTrue(boardDao.insert(boardDto) == 1);
        assertTrue(boardDao.count() == 1);

        assertTrue(boardDao.insert(boardDto) == 1);
        assertTrue(boardDao.count() == 2);
    }

    @Test
    public void deleteAllTest() throws Exception {
        boardDao.deleteAll();
        assertTrue(boardDao.count() == 0);

        BoardDto boardDto = new BoardDto("no title", "no content", "asdf");
        assertTrue(boardDao.insert(boardDto) == 1);
        assertTrue(boardDao.count() == 1);

        assertTrue(boardDao.deleteAll() == 1);
        assertTrue(boardDao.count() == 0);

        assertTrue(boardDao.insert(boardDto) == 1);
        assertTrue(boardDao.insert(boardDto) == 1);
        assertTrue(boardDao.insert(boardDto) == 1);
        assertTrue(boardDao.deleteAll() == 3);
        assertTrue(boardDao.count() == 0);

    }

    @Test
    public void deleteTest() throws Exception {
        boardDao.deleteAll();
        assertTrue(boardDao.count() == 0);

        BoardDto boardDto = new BoardDto("no title", "no content", "asdf");
        assertTrue(boardDao.insert(boardDto) == 1);
        Integer noticeId = boardDao.selectAll().get(0).getNoticeId();
        assertTrue(boardDao.delete(noticeId, boardDto.getNoticeCreatorId()) == 1);
        assertTrue(boardDao.count() == 0);

        assertTrue(boardDao.insert(boardDto) == 1);
        noticeId = boardDao.selectAll().get(0).getNoticeId();
        assertTrue(boardDao.delete(noticeId, boardDto.getNoticeCreatorId() + "222") == 0);
        assertTrue(boardDao.count() == 1);

        assertTrue(boardDao.delete(noticeId, boardDto.getNoticeCreatorId()) == 1);
        assertTrue(boardDao.count() == 0);

        assertTrue(boardDao.insert(boardDto) == 1);
        noticeId = boardDao.selectAll().get(0).getNoticeId();
        assertTrue(boardDao.delete(noticeId + 1, boardDto.getNoticeCreatorId()) == 0);
        assertTrue(boardDao.count() == 1);


    }

    @Test
    public void insertTest() throws Exception {
        boardDao.deleteAll();
        BoardDto boardDto = new BoardDto("no title", "no content", "asdf");
        assertTrue(boardDao.insert(boardDto) == 1);

        boardDto = new BoardDto("no title", "no content", "asdf");
        assertTrue(boardDao.insert(boardDto) == 1);
        assertTrue(boardDao.count() == 2);

        boardDao.deleteAll();
        boardDto = new BoardDto("no title", "no content", "asdf");
        assertTrue(boardDao.insert(boardDto) == 1);
        assertTrue(boardDao.count() == 1);
    }

    @Test
    public void selectAllTest() throws Exception {
        boardDao.deleteAll();
        assertTrue(boardDao.count() == 0);

        List<BoardDto> list = boardDao.selectAll();
        assertTrue(list.size() == 0);

        BoardDto boardDto = new BoardDto("no title", "no content", "asdf");
        assertTrue(boardDao.insert(boardDto) == 1);

        list = boardDao.selectAll();
        assertTrue(list.size() == 1);

        assertTrue(boardDao.insert(boardDto) == 1);
        list = boardDao.selectAll();
        assertTrue(list.size() == 2);
    }

    @Test
    public void selectTest() throws Exception {
        boardDao.deleteAll();
        assertTrue(boardDao.count() == 0);

        BoardDto boardDto = new BoardDto("no title", "no content", "asdf");
        assertTrue(boardDao.insert(boardDto) == 1);

        Integer noticeId = boardDao.selectAll().get(0).getNoticeId();
        boardDto.setNoticeId(noticeId);
        BoardDto boardDto2 = boardDao.select(noticeId);


        assertEquals(boardDto.getNoticeId(), boardDto2.getNoticeId());
        assertEquals(boardDto.getNoticeTitle(), boardDto2.getNoticeTitle());
        assertEquals(boardDto.getNoticeContent(), boardDto2.getNoticeContent());
        assertEquals(boardDto.getNoticeNumber(), boardDto2.getNoticeNumber());
        assertEquals(boardDto.getNoticeCreatorId(), boardDto2.getNoticeCreatorId());
        assertEquals(boardDto.getTopFixedPriority(), boardDto2.getTopFixedPriority());


        assertNotNull(boardDto2.getNoticeCreationTime());

    }
    @Test
    public void selectPageTest() throws Exception {
        boardDao.deleteAll();

        for(int i =1; i<=10; i++){
            BoardDto boardDto = new BoardDto(""+i, "no content"+i, "asdf");
            boardDao.insert(boardDto);
        }
        Map map= new HashMap();
        map.put("offset", 0);
        map.put("pageSize", 3);

        List<BoardDto> list = boardDao.selectPage(map);
        System.out.println(list.get(0).getNoticeTitle());
        assertTrue(list.get(0).getNoticeTitle().equals("10"));
        assertTrue(list.get(1).getNoticeTitle().equals("9"));
        assertTrue(list.get(2).getNoticeTitle().equals("8"));

        map= new HashMap();
        map.put("offset", 0);
        map.put("pageSize", 1);

        list = boardDao.selectPage(map);
        assertTrue(list.get(0).getNoticeTitle().equals("10"));

        map= new HashMap();
        map.put("offset", 7);
        map.put("pageSize", 3);

        list = boardDao.selectPage(map);
        assertTrue(list.get(0).getNoticeTitle().equals("3"));
        assertTrue(list.get(1).getNoticeTitle().equals("2"));
        assertTrue(list.get(2).getNoticeTitle().equals("1"));
    }
    @Test
    public void updateTest() throws Exception {
        boardDao.deleteAll();
        BoardDto boardDto = new BoardDto("no title", "no content", "asdf");
        assertTrue(boardDao.insert(boardDto) == 1);

        Integer noticeId = boardDao.selectAll().get(0).getNoticeId();
        System.out.println("noticeId = " + noticeId);
        boardDto.setNoticeId(noticeId);
        boardDto.setNoticeTitle("yes title");
        assertTrue(boardDao.update(boardDto) == 1);

        BoardDto boardDto2 = boardDao.select(noticeId);
        assertEquals(boardDto.getNoticeTitle(), boardDto2.getNoticeTitle(), "The updated BoardDto should be equal to the original BoardDto");
        assertEquals(boardDto.getNoticeContent(), boardDto2.getNoticeContent());
        assertEquals(boardDto.getNoticeCreatorId(), boardDto2.getNoticeCreatorId());

    }

}

