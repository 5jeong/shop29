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
            m.addAttribute("board", faqDto);
            return "faq/faq";
        } catch (Exception e) {
            logger.error("Error reading FAQ with ID: {}", faqId, e);
            m.addAttribute("error", "Error reading FAQ.");
            return "error";
        }
    }

    @GetMapping("/list")
    public String list(@RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage,
                       @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                       Model m) {
        try {
            int totalCnt = faqService.getCount();
            FaqPageHandler faqPageHandler = new FaqPageHandler(totalCnt, currentPage, pageSize);

            Map<String, Object> map = new HashMap<>();
            map.put("offset", (currentPage - 1) * pageSize);
            map.put("pageSize", pageSize);

            List<FaqDto> list = faqService.getPage(map);

            m.addAttribute("list", list);
            m.addAttribute("page", faqPageHandler); // Add this line
            m.addAttribute("totalCnt", totalCnt);

        } catch (Exception e) {
            LoggerFactory.getLogger(FaqController.class).error("Error retrieving FAQ list", e);
            throw new RuntimeException("Error retrieving FAQ list", e);
        }

        return "faq/list";
    }

    @GetMapping("/edit")
    public String edit(@RequestParam("faqId") Integer faqId, Model m) {
        try {
            FaqDto faqDto = faqService.read(faqId);
            m.addAttribute("faq", faqDto);
            return "faq/edit";
        } catch (Exception e) {
            logger.error("Error editing FAQ with ID: {}", faqId, e);
            m.addAttribute("error", "Error editing FAQ.");
            return "error";
        }
    }

    @PostMapping("/edit")
    public String editSubmit(FaqDto faqDto) {
        try {
            faqService.modify(faqDto);
            return "redirect:/faq/list";
        } catch (Exception e) {
            logger.error("Error modifying FAQ with ID: {}", faqDto.getFaqId(), e);
            return "redirect:/error";
        }
    }

    @GetMapping("/write")
    public String writeForm(Model m) {
        m.addAttribute("faq", new FaqDto());
        return "faq/write";
    }

    @PostMapping("/write")
    public String writeSubmit(FaqDto faqDto) {
        try {
            faqDto.setFaqCreatorId("관리자");
            if (faqDto.getFaqTypeId() == null || faqDto.getFaqTypeId().isEmpty()) {
                faqDto.setFaqTypeId("defaultType"); // 적절한 기본값 설정
            }
            faqService.write(faqDto);
            return "redirect:/faq/list";
        } catch (Exception e) {
            logger.error("Error writing FAQ", e);
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/delete")
    public String deletefaq(@RequestParam("faqId") Integer faqId, @RequestParam("faqCreatorId") String faqCreatorId) {
        try {
            logger.info("Deleting FAQ with ID: {} and creator ID: {}", faqId, faqCreatorId);
            faqService.remove(faqId, faqCreatorId);
            return "redirect:/faq/list";
        } catch (Exception e) {
            logger.error("Error deleting FAQ with ID: {}", faqId, e);
            return "redirect:/error";
        }
    }
}
