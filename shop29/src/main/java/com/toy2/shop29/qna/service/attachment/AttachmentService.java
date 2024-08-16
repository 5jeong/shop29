package com.toy2.shop29.qna.service.attachment;


import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * AttachmentService 는 QnaService에서 사용될 목적으로 만들어짐
 * 따라서, 계정 권한과 관련된 부분은 고려하지 않았음
 */
public interface AttachmentService {

    // [CREATE] 첨부파일 여러건 저장
    void createAttachments(int qnaId, List<File> files) throws IOException;

    // [DELETE] 첨부파일 여러건 삭제
    void deleteAttachmentsBy(int qnaId) throws IOException;
}
