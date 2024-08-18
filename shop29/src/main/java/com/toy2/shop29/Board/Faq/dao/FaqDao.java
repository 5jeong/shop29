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

    // 검색어를 포함한 총 FAQ 수를 가져오는 메서드
    int countBySearchQuery(String searchQuery);

    // 검색어를 포함한 페이지 데이터를 가져오는 메서드
    List<FaqDto> selectPageWithSearchQuery(Map<String, Object> map);

};
