package com.toy2.shop29.qna.service.attachment;


import com.toy2.shop29.qna.domain.AttachmentTableName;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * AttachmentService 는 QnaService에서 사용될 목적으로 만들어짐
 * 따라서, 계정 권한과 관련된 부분은 고려하지 않았음
 */
public interface AttachmentService {
    // [READ] 파일의 MIME 타입 가져오기
    String getMimeType(Resource resource);

    // [READ] 파일 저장소에서 첨부파일 가져오기
    Resource getResourceFromFile(String fileName) throws RuntimeException;

    // [CREATE] 파일 저장소에 첨부파일 저장
    String saveMultipartFile(MultipartFile multipartFile) throws RuntimeException;

    // [CREATE] 첨부파일 여러건 저장
    void createAttachments(String userId, int tableId, AttachmentTableName tableName, List<String> attachmentNames) throws RuntimeException;

    // [DELETE] 첨부파일 여러건 삭제
    void deleteAttachmentsBy(String userId,int tableId, AttachmentTableName tableName) throws RuntimeException;
}
