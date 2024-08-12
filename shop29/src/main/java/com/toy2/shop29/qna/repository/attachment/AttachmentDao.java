package com.toy2.shop29.qna.repository.attachment;

import com.toy2.shop29.qna.domain.AttachmentDto;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface AttachmentDao {

    // Create
    int insert(AttachmentDto attachmentDto) throws DataAccessException;
    int insertList(List<AttachmentDto> attachmentDtos) throws DataAccessException;

    // Read
    AttachmentDto select(int attachmentId, Boolean isActive) throws DataAccessException;
    List<AttachmentDto> selectAll(Boolean isActive) throws DataAccessException;
    int count() throws DataAccessException;

    // Update
    int update(AttachmentDto attachmentDto) throws DataAccessException;

    // Delete
    int deleteAll() throws DataAccessException;
}
