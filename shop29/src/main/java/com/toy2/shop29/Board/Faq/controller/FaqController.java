package com.toy2.shop29.Board.Faq.controller;

import com.toy2.shop29.Board.Faq.domain.FaqDto;
import com.toy2.shop29.Board.Faq.domain.FaqPageHandler;
import com.toy2.shop29.Board.Faq.service.FaqService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping("/faq")
public class FaqController {

    private static final Logger logger = LoggerFactory.getLogger(FaqController.class);

    @Autowired
    private FaqService faqService;

    @GetMapping("/read")
    public String read(@RequestParam("faqId") Integer faqId, Model m) {
        try {
            FaqDto faqDto = faqService.read(faqId);
            m.addAttribute("faq", faqDto);
        } catch (Exception e) {
            logger.error("Error reading FAQ with ID: {}", faqId, e);
            throw new RuntimeException("Error reading FAQ", e);
        }
        return "faq/faq";
    }

    @GetMapping("/list")
    public String list(@RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage,
                       @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                       @RequestParam(value = "searchQuery", defaultValue = "") String searchQuery,
                       @RequestParam(value = "option", defaultValue = "A") String option, // 검색 옵션 추가
                       Model m) {
        try {
            int totalCnt = faqService.getCountWithSearchQuery(option, searchQuery); // 검색어를 고려하여 총 FAQ 수를 가져옵니다.
            FaqPageHandler faqPageHandler = new FaqPageHandler(totalCnt, currentPage, pageSize);

            Map<String, Object> map = new HashMap<>();
            map.put("offset", (currentPage - 1) * pageSize);
            map.put("pageSize", pageSize);
            map.put("searchQuery", searchQuery); // 검색어를 추가합니다.
            map.put("option", (int)option.charAt(0)); // 검색 옵션 추가

            // 검색 결과 가져오기
            List<FaqDto> list = faqService.getPageWithSearch(map);
            if (list == null || list.isEmpty()) {
                m.addAttribute("error", "No FAQs found.");
            }

            m.addAttribute("list", list);
            m.addAttribute("page", faqPageHandler);
            m.addAttribute("totalCnt", totalCnt);
            m.addAttribute("searchQuery", searchQuery); // 검색어를 모델에 추가합니다.
            m.addAttribute("option", option); // 검색 옵션 추가
        } catch (Exception e) {
            logger.error("Error listing FAQs", e);
            throw new RuntimeException("Error listing FAQs", e);
        }
        return "faq/list";
    }

    // FAQ 수정 페이지
    @GetMapping("/edit")
    public String edit(@RequestParam("faqId") Integer faqId, Model m) {
        try {
            FaqDto faqDto = faqService.read(faqId);
            m.addAttribute("faq", faqDto);
        } catch (Exception e) {
            logger.error("Error retrieving FAQ for editing with ID: {}", faqId, e);
            throw new RuntimeException("Error retrieving FAQ for editing", e);
        }
        return "faq/edit";
    }

    @PostMapping("/edit")
    public String editSubmit(FaqDto faqDto) {
        try {
            faqService.modify(faqDto); // 수정된 부분
        } catch (Exception e) {
            logger.error("Error submitting FAQ edit: {}", faqDto, e);
            throw new RuntimeException("Error submitting FAQ edit", e); // 수정된 부분
        }
        return "redirect:/faq/list";
    }

    @GetMapping("/write")
    public String writeForm(Model model) {
        model.addAttribute("faq", new FaqDto());
        return "faq/write";
    }

    // FAQ 작성 처리
    @PostMapping("/write")
    public String writeSubmit(FaqDto faqDto) {
        try {
            faqDto.setFaqCreatorId("관리자");
            faqService.write(faqDto);
        } catch (Exception e) {
            logger.error("Error writing FAQ: {}", faqDto, e);
            throw new RuntimeException("Error writing FAQ", e);
        }
        return "redirect:/faq/list"; // 작성 후 목록 페이지로 리다이렉션
    }

    // FAQ 삭제 처리
    @PostMapping("/delete")
    public String deletefaq(@RequestParam("faqId") Integer faqId, @RequestParam("faqCreatorId") String faqCreatorId) {
        try {
            System.out.println("Deleting FAQ with ID: " + faqId);
            System.out.println("Deleting FAQ with Creator ID: " + faqCreatorId);
            int deleteResult = faqService.remove(faqId, faqCreatorId);
        } catch (Exception e) {
            logger.error("Error deleting FAQ with ID: {} and Creator ID: {}", faqId, faqCreatorId, e);
            throw new RuntimeException("Error deleting FAQ", e);
        }
        return "redirect:/faq/list";
    }
}
