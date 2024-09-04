package com.toy2.shop29.Board.Faq.dao;

import com.toy2.shop29.Board.Faq.domain.FaqDto;

import java.util.List;
import java.util.Map;

public interface FaqDao {

    /**
     * 특정 FAQ의 상세 정보를 조회합니다.
     *
     * @param faqId 조회할 FAQ의 ID
     * @return 특정 FAQ의 정보
     */
    FaqDto select(Integer faqId);

    /**
     * 특정 FAQ를 삭제합니다.
     *
     * @param faqId 삭제할 FAQ의 ID
     * @param faqCreatorId 삭제를 요청한 사용자 ID
     * @return 삭제된 FAQ의 수
     */
    int delete(Integer faqId, String faqCreatorId);

    /**
     * 새로운 FAQ를 추가합니다.
     *
     * @param faq 추가할 FAQ의 정보
     * @return 삽입된 FAQ의 수
     */
    int insert(FaqDto faq);

    /**
     * 특정 FAQ를 수정합니다.
     *
     * @param faq 수정할 FAQ의 정보
     * @return 수정된 FAQ의 수
     */
    int update(FaqDto faq);

    /**
     * 모든 FAQ 목록을 조회합니다.
     *
     * @return 전체 FAQ 목록
     */
    List<FaqDto> selectAll();

    /**
     * 페이지네이션에 따른 FAQ 목록을 조회합니다.
     *
     * @param map 페이지 정보 (offset, pageSize 등)
     * @return 페이지에 해당하는 FAQ 목록
     */
    List<FaqDto> selectPage(Map<String, Object> map);

    /**
     * 모든 FAQ를 삭제합니다.
     *
     * @return 삭제된 FAQ의 수
     */
    int deleteAll();

    /**
     * 전체 FAQ 개수를 조회합니다.
     *
     * @return 전체 FAQ 개수
     */
    int count();

    /**
     * 검색어와 옵션을 기준으로 총 FAQ 개수를 반환합니다.
     *
     * @param params 검색 조건 (option, searchQuery 등)
     * @return 검색 조건을 만족하는 FAQ 개수
     */
    int countBySearchQuery(Map<String, Object> params);

    /**
     * 검색어와 페이지 조건에 따라 FAQ 리스트를 반환합니다.
     *
     * @param params 페이지 정보와 검색 조건
     * @return 검색 조건과 페이지에 해당하는 FAQ 목록
     */
    List<FaqDto> selectPageWithSearch(Map<String, Object> params);
}
