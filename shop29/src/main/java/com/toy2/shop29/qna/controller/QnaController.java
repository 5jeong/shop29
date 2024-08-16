package com.toy2.shop29.qna.controller;

import com.toy2.shop29.qna.domain.QnaTypeDto;
import com.toy2.shop29.qna.domain.request.QnaCreateRequest;
import com.toy2.shop29.qna.domain.response.QnaResponse;
import com.toy2.shop29.qna.service.qna.QnaService;
import com.toy2.shop29.qna.service.qnatype.QnaTypeService;
import com.toy2.shop29.qna.util.QnaPagingHandler;
import com.toy2.shop29.users.domain.UserDto;
import com.toy2.shop29.users.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/qna")
public class QnaController {

    private QnaService qnaService;
    private QnaTypeService qnaTypeService;
    private UserService userService;

    public QnaController(QnaService qnaService, QnaTypeService qnaTypeService, UserService userService) {
        this.qnaService = qnaService;
        this.qnaTypeService = qnaTypeService;
        this.userService = userService;
    }

    // TODO: 세션에서 사용자 ID 받아 처리 필요
    @GetMapping("/form")
    String get(
            @SessionAttribute(name = "loginUser", required = false) String loginUser, Model model){
        // key : 부모 문의유형 이름, value : 자식 문의유형 리스트
        Map<String,List<QnaTypeDto>> qnaTypeMap = qnaTypeService.findAllWithParentForUser();
        model.addAttribute("qnaTypeMap", qnaTypeMap);

        // 자식문의유형 리스트
        List<QnaTypeDto> qnaTypeDtos = qnaTypeService.findAll();
        model.addAttribute("qnaTypeList", qnaTypeDtos);

        // 사용자 정보
        UserDto userDto = userService.findById(loginUser);
        model.addAttribute("userDto", userDto);

        return "qna/qnaForm";
    }

    // TODO: 세션에서 사용자 ID 받아 처리 필요
    @GetMapping("/qna-list")
    String getQnaList(
            @SessionAttribute(name = "loginUser", required = true) String loginUser,
            @RequestParam(name = "pageNo",defaultValue = "1") int pageNo,
            @RequestParam(name = "pageSize",defaultValue = "10") int pageSize,
            Model model){
        int totalCnt = qnaService.countByUserId(loginUser);
        QnaPagingHandler pagingHandler = new QnaPagingHandler(totalCnt, pageSize, 10,pageNo);
        int limit = pagingHandler.getLimit();
        int offset = pagingHandler.getOffset();

        List<QnaResponse> qnaResponses = qnaService.findQnaList(loginUser, limit, offset);
        model.addAttribute("qnaResponses", qnaResponses);
        model.addAttribute("ph", pagingHandler);

        return "qna/qnaList";
    }

    @PostMapping
    String post(
            @SessionAttribute(name = "loginUser", required = true) String loginUser,
            @ModelAttribute QnaCreateRequest qnaCreateRequest){
        try{
            qnaService.createQna(qnaCreateRequest, loginUser);
        }catch (RuntimeException e){
            return "redirect:qna/error";
        }

        return "redirect:qna/qna-list";
    }

    @DeleteMapping
    @ResponseBody
    ResponseEntity<String> delete(
            @SessionAttribute(name = "loginUser", required = false) String userId,
            @RequestParam(name= "qnaId",required = true) int qnaId
    ){
        try{
            qnaService.deleteQna(qnaId, userId);
        }catch (RuntimeException e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("true", HttpStatus.OK);
    }
}
