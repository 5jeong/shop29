package com.toy2.shop29.qna.service.qnatype;

import com.toy2.shop29.qna.domain.QnaTypeDto;
import java.util.List;
import java.util.Map;

public interface QnaTypeService {

    List<QnaTypeDto> findAll() throws RuntimeException;

    /**
     * [READ] 문의유형 전체조회 -> 사용자용
     */
    Map<String, List<QnaTypeDto>> findAllWithParentForUser() throws RuntimeException;

    /**
     * [READ] 문의유형 전체조회 -> 관리자용
     */
    List<QnaTypeDto> findAllWithParentForAdmin() throws RuntimeException;

    /**
     * [READ] 특정 문의유형ID를 기준으로, 문의유형 & 부모문의유형 조회
     */
    QnaTypeDto findByQnaTypeIdWithParent(String qnaTypeId) throws RuntimeException;
}
