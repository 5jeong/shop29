package com.toy2.shop29.Board.Notice.controller;

import com.toy2.shop29.Board.Notice.domain.BoardDto;
import com.toy2.shop29.Board.Notice.domain.PageHandler;
import com.toy2.shop29.Board.Notice.service.BoardService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/board")
public class BoardController {
    @Autowired
    BoardService boardService;

    @GetMapping("/read")
    public String read(@RequestParam("noticeId") Integer noticeId, Model m) {
        try {
            BoardDto boardDto = boardService.read(noticeId);
            m.addAttribute("board", boardDto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "board/board";
    }

    @GetMapping("/list")
    public String list(@RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage,
                       @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                       Model m, HttpServletRequest request) {
        try {
            int totalCnt = boardService.getCount();
            PageHandler pageHandler = new PageHandler(totalCnt, currentPage, pageSize);
            Map<String, Object> map = new HashMap<>();
            map.put("offset", (currentPage - 1) * pageSize);
            map.put("pageSize", pageSize);

            List<BoardDto> list = boardService.getPage(map);
            m.addAttribute("list", list);
            m.addAttribute("ph", pageHandler);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return "board/boardlist";
    }

    @GetMapping("/edit")
    public String edit(@RequestParam("noticeId") Integer noticeId, Model m) {
        try {
            BoardDto boardDto = boardService.read(noticeId);
            m.addAttribute("board", boardDto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "board/edit"; // 수정 폼을 보여주는 뷰 이름
    }

    @PostMapping("/edit")
    public String editSubmit(BoardDto boardDto) {
        try {
            boardService.modify(boardDto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "redirect:/board/list"; // 수정 후 목록 페이지로 리다이렉션
    }

    @GetMapping("/write")
    public String writeForm() {
        return "board/write"; // 작성 폼을 보여주는 뷰 이름
    }

    @PostMapping("/write")
    public String writeSubmit(BoardDto boardDto) {
        try {
            // 공지사항 작성자는 세션에서 가져오거나, 현재 로그인 사용자 정보에서 설정
            // 예를 들어, 현재 사용자 ID를 가져오는 방법을 적용하세요.
            boardDto.setNoticeCreatorId("관리자"); // 이 부분을 실제 사용자 ID로 설정해야 합니다.
            boardService.write(boardDto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "redirect:/board/list"; // 작성 후 목록 페이지로 리다이렉션
    }

    @PostMapping("/delete")
    public String deleteNotice(@RequestParam("noticeId") Integer noticeId, @RequestParam("noticeCreatorId") String noticeCreatorId) {
        System.out.println("Deleting notice with ID: " + noticeId); // 로그 추가
        System.out.println("Deleting notice with noticeCreatorId: " + noticeCreatorId); // 로그 추가
        int deleteResult = boardService.remove(noticeId, noticeCreatorId);
        return "redirect:/board/list";
    }
}
