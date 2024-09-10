package com.toy2.shop29.qna.controller;

import com.toy2.shop29.qna.domain.QnaTypeDto;
import com.toy2.shop29.qna.domain.request.QnaCreateRequest;
import com.toy2.shop29.qna.domain.response.QnaAdminResponse;
import com.toy2.shop29.qna.domain.response.QnaDetailResponse;
import com.toy2.shop29.qna.domain.response.QnaResponse;
import com.toy2.shop29.qna.service.qna.QnaService;
import com.toy2.shop29.qna.service.qnaanswer.QnaAnswerService;
import com.toy2.shop29.qna.service.qnatype.QnaTypeService;
import com.toy2.shop29.qna.util.QnaPagingHandler;
import com.toy2.shop29.users.domain.UserContext;
import com.toy2.shop29.users.domain.UserDto;
import com.toy2.shop29.users.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    private QnaAnswerService qnaAnswerService;
    private final String ROLE_ADMIN = "ROLE_ADMIN";

    public QnaController(QnaService qnaService, QnaTypeService qnaTypeService, UserService userService, QnaAnswerService qnaAnswerService) {
        this.qnaService = qnaService;
        this.qnaTypeService = qnaTypeService;
        this.userService = userService;
        this.qnaAnswerService = qnaAnswerService;
    }

    @GetMapping("/form")
    String getQnaForm(
            @AuthenticationPrincipal UserContext userContext, Model model){
        // key : 부모 문의유형 이름, value : 자식 문의유형 리스트
        Map<String,List<QnaTypeDto>> qnaTypeMap = qnaTypeService.findAllWithParentForUser();
        model.addAttribute("qnaTypeMap", qnaTypeMap);

        // 자식문의유형 리스트
        List<QnaTypeDto> qnaTypeDtos = qnaTypeService.findAll();
        model.addAttribute("qnaTypeList", qnaTypeDtos);

        // 사용자 정보
//        UserDto userDto = userService.findById(loginUser);
        model.addAttribute("userDto", userContext.getUserDto());

        return "qna/qnaForm";
    }

    @GetMapping("/qna-list")
    String getQnaList(
            @AuthenticationPrincipal UserContext userContext,
            @RequestParam(name = "pageNo",defaultValue = "1") int pageNo,
            @RequestParam(name = "pageSize",defaultValue = "10") int pageSize,
            Model model){
        UserDto userDto = userContext.getUserDto();
        String userRole = userDto.getUserRole();

        if(userRole.equals(ROLE_ADMIN)){
            return "redirect:/qna/admin/qna-list";
        }

        int totalCnt = qnaService.countByUserId(userDto.getUserId());
        QnaPagingHandler pagingHandler = new QnaPagingHandler(totalCnt, pageSize, 10,pageNo);
        int limit = pagingHandler.getLimit();
        int offset = pagingHandler.getOffset();

        List<QnaResponse> qnaResponses = qnaService.findQnaList(userDto.getUserId(), limit, offset);
        model.addAttribute("qnaResponses", qnaResponses);
        model.addAttribute("ph", pagingHandler);

        return "qna/qnaList";
    }

    /**
     * 이 메서드는 챗봇을 위한 것이며, 오직 localhost에서만 접근 가능하도록 제한해야합니다.
     */
    @ResponseBody
    @GetMapping("/qna-list/{userId}")
    List<QnaResponse> getQnaListRest(
            @PathVariable(name = "userId") String userId){
        return qnaService.findQnaListAll(userId);
    }

    @GetMapping("/admin/qna-list")
    String getQnaListForAdmin(
            @AuthenticationPrincipal UserContext userContext,
            @RequestParam(name = "pageNo",defaultValue = "1") int pageNo,
            @RequestParam(name = "pageSize",defaultValue = "10") int pageSize,
            @RequestParam(name = "qnaTypeId",required = false) String qnaTypeId,
            @RequestParam(name = "isAnswered",required = false) Boolean isAnswered,
            Model model){
        UserDto userDto = userContext.getUserDto();
        String userRole = userDto.getUserRole();

        if(!userRole.equals(ROLE_ADMIN)){
            return "redirect:/qna/qna-list";
        }

        int totalCnt = qnaService.countForAdminWithFilter(qnaTypeId, isAnswered);
        QnaPagingHandler pagingHandler = new QnaPagingHandler(totalCnt, pageSize, 10,pageNo);
        int limit = pagingHandler.getLimit();
        int offset = pagingHandler.getOffset();

        List<QnaAdminResponse> qnaResponses = qnaService.findQnaListWithFilter(limit, offset, qnaTypeId, isAnswered);

        model.addAttribute("qnaTypes", qnaTypeService.findAll());
        model.addAttribute("qnaTypeId", qnaTypeId);
        model.addAttribute("isAnswered", isAnswered);
        model.addAttribute("qnaResponses", qnaResponses);
        model.addAttribute("ph", pagingHandler);

        return "qna/qnaAdminList";
    }



    @GetMapping("/answer")
    String getAnswerForm(
            @AuthenticationPrincipal UserContext userContext,
            @RequestParam(name = "qnaId", required = true) int qnaId,
            Model model){
        UserDto userDto = userContext.getUserDto();
        String userRole = userDto.getUserRole();

        if(!userRole.equals(ROLE_ADMIN)){
            return "redirect:/qna/qna-list";
        }

        QnaDetailResponse response = qnaService.findQnaDetail(qnaId);
        model.addAttribute("response", response);

        return "qna/qnaAnswerForm";
    }

    @PostMapping
    ResponseEntity<String> postQna(
            @AuthenticationPrincipal UserContext userContext,
            @ModelAttribute @Valid QnaCreateRequest qnaCreateRequest){
        try{
            UserDto userDto = userContext.getUserDto();
            qnaService.createQna(qnaCreateRequest, userDto.getUserId());
        }catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        // 리다이렉트할 URL
        String redirectUrl = "/qna/qna-list";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", redirectUrl);

        // ResponseEntity를 생성하여 리다이렉트 응답 반환
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @DeleteMapping
    @ResponseBody
    ResponseEntity<String> deleteQna(
            @AuthenticationPrincipal UserContext userContext,
            @RequestParam(name= "qnaId",required = true) int qnaId
    ){
        try{
            UserDto userDto = userContext.getUserDto();
            qnaService.deleteQna(qnaId, userDto.getUserId());
        }catch (RuntimeException e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("true", HttpStatus.OK);
    }

    @PostMapping("/answer")
    @ResponseBody
    ResponseEntity<String> postAnswer(
            @AuthenticationPrincipal UserContext userContext,
            @RequestParam(name = "qnaId", required = true) int qnaId,
            @RequestParam(name = "answerContent", required = true) String answerContent){
        try{
            UserDto userDto = userContext.getUserDto();
            qnaAnswerService.createQnaAnswer(qnaId, userDto.getUserId(), answerContent);
        }catch (RuntimeException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("true", HttpStatus.OK);
    }

    @PutMapping("/answer")
    @ResponseBody
    ResponseEntity<String> putAnswer(
            @AuthenticationPrincipal UserContext userContext,
            @RequestParam(name = "qnaAnswerId", required = true) int qnaAnswerId,
            @RequestParam(name = "answerContent", required = true) String answerContent){
        try{
            UserDto userDto = userContext.getUserDto();
            qnaAnswerService.updateQnaAnswer(qnaAnswerId, userDto.getUserId(), answerContent);
        }catch (RuntimeException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("true", HttpStatus.OK);
    }
}
