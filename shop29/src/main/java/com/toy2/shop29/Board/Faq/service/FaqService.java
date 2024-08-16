package com.toy2.shop29.Board.Faq.service;

import com.toy2.shop29.Board.Faq.domain.FaqDto;

import java.util.List;
import java.util.Map;

public interface FaqService {

    /**
     * 총 FAQ 개수를 반환합니다.
     *
     * @return 총 FAQ 개수
     */
    int getCount();

    /**
     * 특정 FAQ를 삭제합니다.
     *
     * @param faqId 삭제할 FAQ의 ID
     * @param faqCreatorId FAQ 작성자의 ID
     * @return 삭제된 FAQ의 수
     */
    int remove(Integer faqId, String faqCreatorId);

    /**
     * 새로운 FAQ를 작성합니다.
     *
     * @param faqDto 작성할 FAQ의 데이터
     * @return 작성된 FAQ의 ID
     */
    int write(FaqDto faqDto);

    /**
     * 전체 FAQ 목록을 반환합니다.
     *
     * @return FAQ 목록
     */
    List<FaqDto> getList();

    /**
     * 특정 FAQ의 상세 정보를 반환합니다.
     *
     * @param faqId 조회할 FAQ의 ID
     * @return FAQ의 상세 정보
     */
    FaqDto read(Integer faqId);

    /**
     * 페이지 정보를 기준으로 FAQ 목록을 반환합니다.
     *
     * @param map 페이지 처리 정보 (offset, pageSize 등)
     * @return 페이지에 해당하는 FAQ 목록
     */
    List<FaqDto> getPage(Map<String, Object> map);

    /**
     * 특정 FAQ의 정보를 수정합니다.
     *
     * @param faqDto 수정할 FAQ의 데이터
     * @return 수정된 FAQ의 수
     */
    int modify(FaqDto faqDto);
}
