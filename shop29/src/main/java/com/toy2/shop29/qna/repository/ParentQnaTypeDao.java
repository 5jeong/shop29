package com.toy2.shop29.qna.repository;

import com.toy2.shop29.qna.dto.ParentQnaTypeDto;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface ParentQnaTypeDao {

    // Create
    int insert(ParentQnaTypeDto parentQnaTypeDto) throws DataAccessException;

    // Read
    ParentQnaTypeDto select(String parentQnaTypeId) throws DataAccessException;
    List<ParentQnaTypeDto> selectAll() throws DataAccessException;
    int count() throws DataAccessException;

    // Update
    int update(ParentQnaTypeDto parentQnaTypeDto) throws DataAccessException;

    // Delete
    int delete(String parentQnaTypeId) throws DataAccessException;
    int deleteAll() throws DataAccessException;
}
