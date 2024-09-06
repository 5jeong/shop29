package com.toy2.shop29.qna.repository.qna;

import com.toy2.shop29.qna.domain.QnaDto;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface QnaDao {

    // Create
    int insert(QnaDto qnaDto) throws DataAccessException;

    // Read

    /**
     * 개인 조회목적, 조인 : 문의유형, 1:1문의, 문의답변, 첨부파일
     */
    List<QnaDto> selectAllWith(String userId, Integer limit, Integer offset, Boolean isActive) throws DataAccessException;
    //

    /**
     * 관리자 조회목적, 조인 : 문의유형, 1:1문의, 유저 정보, 문의답변
     */
    List<QnaDto> selectAllWithFilter(int limit, int offset, String qnaTypeId,  Boolean isAnswered, Boolean isActive) throws DataAccessException;

    /**
     * 관리자 답글 작성 페이지 內 정보조회 목적, 조인 : 문의유형, 부모문의유형, 1:1 문의글, 첨부파일, 유저정보, 문의답변
     */
    QnaDto selectWith(int qnaId) throws DataAccessException;

    QnaDto select(int qnaId, Boolean isDeleted) throws DataAccessException; // 테스트용
    //    List<QnaDto> selectAll() throws DataAccessException; // 테스트용
    int countByUserId(String userId) throws DataAccessException;
    int countForAdminWithFilter(String qnaTypeId, Boolean isAnswered) throws DataAccessException;
    int count() throws DataAccessException;

    // Update
    int update(QnaDto qnaDto) throws DataAccessException;

    // Delete
    int softDelete(int qnaId, String userId) throws DataAccessException;
    int deleteAll() throws DataAccessException;
}
