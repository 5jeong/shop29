package com.toy2.shop29.Board.Faq.service;

import com.toy2.shop29.Board.Faq.dao.FaqDao;
import com.toy2.shop29.Board.Faq.domain.FaqDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class FaqServiceImpl implements FaqService {

    private static final Logger logger = LoggerFactory.getLogger(FaqServiceImpl.class);

    @Autowired
    private FaqDao faqDao;

    @Override
    public int getCount() {
        try {
            return faqDao.count();
        } catch (Exception e) {
            logger.error("Error getting FAQ count", e);
            throw new RuntimeException("Error getting FAQ count", e);
        }
    }

    @Override
    @Transactional
    public int remove(Integer faqId, String faqCreatorId) {
        try {
            return faqDao.delete(faqId, faqCreatorId);
        } catch (Exception e) {
            logger.error("Error removing FAQ with ID: {} and creator ID: {}", faqId, faqCreatorId, e);
            throw new RuntimeException("Error removing FAQ", e);
        }
    }

    @Override
    @Transactional
    public int write(FaqDto faqDto) {
        try {
            return faqDao.insert(faqDto);
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
            logger.error("Error getting FAQ list", e);
            throw new RuntimeException("Error getting FAQ list", e);
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
            logger.error("Error getting FAQ page with parameters: {}", map, e);
            throw new RuntimeException("Error getting FAQ page", e);
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
