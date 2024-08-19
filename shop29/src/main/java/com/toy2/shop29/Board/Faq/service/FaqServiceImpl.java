package com.toy2.shop29.Board.Faq.service;

import com.toy2.shop29.Board.Faq.dao.FaqDao;
import com.toy2.shop29.Board.Faq.domain.FaqDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FaqServiceImpl implements FaqService {

    private static final Logger logger = LoggerFactory.getLogger(FaqServiceImpl.class);

    @Autowired
    private FaqDao faqDao;

    @Override
    public int getCountWithSearchQuery(String option, String searchQuery) {
        try {
            // Map을 생성하여 검색 옵션과 검색어를 전달합니다.
//            Map<String, Object> params = Map.of("option", option, "searchQuery", searchQuery);
            Map<String, Object> params = new HashMap<>();
            params.put("option", (int)option.charAt(0));
            params.put("searchQuery", searchQuery);

            return faqDao.countBySearchQuery(params);
        } catch (Exception e) {
            logger.error("Error counting FAQs with option: {} and searchQuery: {}", option, searchQuery, e);
            throw new RuntimeException("Error counting FAQs", e);
        }
    }

    @Override
    public List<FaqDto> getPageWithSearch(Map<String, Object> map) {
        try {
            return faqDao.selectPageWithSearch(map);
        } catch (Exception e) {
            logger.error("Error retrieving FAQ page with search criteria: {}", map, e);
            throw new RuntimeException("Error retrieving FAQ page with search criteria", e);
        }
    }

    @Override
    @Transactional
    public int remove(Integer faqId, String faqCreatorId) {
        try {
            return faqDao.delete(faqId, faqCreatorId);
        } catch (Exception e) {
            logger.error("Error removing FAQ with ID: {} and Creator ID: {}", faqId, faqCreatorId, e);
            throw new RuntimeException("Error removing FAQ", e);
        }
    }

    @Override
    @Transactional
    public int write(FaqDto faqDto) {
        try {
            faqDao.insert(faqDto);
            return faqDto.getFaqId(); // ID가 자동으로 설정되었다고 가정
        } catch (Exception e) {
            logger.error("Error writing FAQ: {}", faqDto, e);
            throw new RuntimeException("Error writing FAQ", e);
        }
    }

    @Override
    public List<FaqDto> getList() {
        try {
            return faqDao.selectAll();
        } catch (Exception e) {
            logger.error("Error retrieving FAQ list", e);
            throw new RuntimeException("Error retrieving FAQ list", e);
        }
    }

    @Override
    public FaqDto read(Integer faqId) {
        try {
            return faqDao.select(faqId);
        } catch (Exception e) {
            logger.error("Error reading FAQ with ID: {}", faqId, e);
            throw new RuntimeException("Error reading FAQ", e);
        }
    }

    @Override
    public List<FaqDto> getPage(Map<String, Object> map) {
        try {
            return faqDao.selectPage(map);
        } catch (Exception e) {
            logger.error("Error retrieving FAQ page with map: {}", map, e);
            throw new RuntimeException("Error retrieving FAQ page", e);
        }
    }

    @Override
    @Transactional
    public int modify(FaqDto faqDto) {
        try {
            return faqDao.update(faqDto);
        } catch (Exception e) {
            logger.error("Error modifying FAQ: {}", faqDto, e);
            throw new RuntimeException("Error modifying FAQ", e);
        }
    }
}
