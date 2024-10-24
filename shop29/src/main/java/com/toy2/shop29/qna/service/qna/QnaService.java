package com.toy2.shop29.qna.service.qna;

import com.toy2.shop29.qna.domain.request.QnaCreateRequest;
import com.toy2.shop29.qna.domain.response.QnaAdminResponse;
import com.toy2.shop29.qna.domain.response.QnaDetailResponse;
import com.toy2.shop29.qna.domain.response.QnaResponse;

import java.util.List;

public interface QnaService {

    List<QnaResponse> findQnaListAll(String userId) throws RuntimeException;

    /**
     * [READ] 1:1 문의 전체조회
     * @param limit 0이면 개수제한 X & offset 미적용
     */
    List<QnaResponse> findQnaList(String userId, int limit, int offset) throws RuntimeException;

    // [READ] 1:1 문의 전체조회 <- 관리자 페이지 정보제공 목적
    List<QnaAdminResponse> findQnaListWithFilter(int limit, int offset, String qnaTypeId, Boolean isAnswered) throws RuntimeException;

    // [READ] 1:1 문의 상세조회 <- 관리자 답글 작성 페이지 정보제공 목적
    QnaDetailResponse findQnaDetail(int qnaId) throws RuntimeException;

    int countByUserId(String userId) throws RuntimeException;

    int countForAdminWithFilter(String qnaTypeId, Boolean isAnswered) throws RuntimeException;

    // [CREATE] 1:1 문의 등록
    void createQna(QnaCreateRequest request, String userId) throws RuntimeException;

    // [DELETE] 1:1 문의 삭제 <- softDelete
    void deleteQna(int qnaId, String userId) throws RuntimeException;
}
