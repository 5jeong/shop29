package com.toy2.shop29.qna.repository.qnatype;

import com.toy2.shop29.qna.domain.QnaTypeDto;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface QnaTypeDao {

    // Create
    int insert(QnaTypeDto qnaTypeDto) throws DataAccessException;

    // Read
    List<QnaTypeDto> selectAll(boolean withParent, Boolean isActive) throws DataAccessException;
    QnaTypeDto select(String qnaTypeId, boolean withParent, Boolean isActive) throws DataAccessException;
    int count() throws DataAccessException;

    // Update
    int update(QnaTypeDto qnaTypeDto) throws DataAccessException;

    // Delete
    int delete(String qnaTypeId) throws DataAccessException;
    int deleteAll() throws DataAccessException;
}
