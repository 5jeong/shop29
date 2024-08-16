package com.toy2.shop29.Board.Faq.dao;

import com.toy2.shop29.Board.Faq.domain.FaqDto;

import java.util.List;
import java.util.Map;

public interface FaqDao {
    FaqDto select(Integer faqId);

    int delete(Integer faqId, String faqCreatorId);

    int insert(FaqDto faq);

    int update(FaqDto faq);

    List<FaqDto> selectAll();

    List<FaqDto> selectPage(Map<String, Object> map);

    int deleteAll();

    int count();
};
