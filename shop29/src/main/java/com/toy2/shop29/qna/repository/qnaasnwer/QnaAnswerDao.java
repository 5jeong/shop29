package com.toy2.shop29.qna.repository.qnaasnwer;

import com.toy2.shop29.qna.domain.QnaAnswerDto;
import org.springframework.dao.DataAccessException;
import java.util.List;

public interface QnaAnswerDao {

    // Create
    int insert(QnaAnswerDto qnaAnswerDto) throws DataAccessException;

    // Read
    QnaAnswerDto selectBy(int qnaId, Boolean isDeleted) throws DataAccessException;
    // 테스트 목적
    QnaAnswerDto select(int qnaAnswerId, Boolean isDeleted) throws DataAccessException;
    // 테스트 목적
    List<QnaAnswerDto> selectAll(Boolean isDeleted) throws DataAccessException;
    int count() throws DataAccessException;

    // Update
    int update(QnaAnswerDto qnaAnswerDto) throws DataAccessException;

    // Delete
    int softDelete(int qnaAnswerId, String userId) throws DataAccessException;
    int deleteAll() throws DataAccessException;
}
