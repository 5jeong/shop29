package com.toy2.shop29.qna.service.attachment;


import com.toy2.shop29.qna.domain.AttachmentDto;
import com.toy2.shop29.qna.domain.AttachmentTableName;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AttachmentService {

    /**
     * [CREATE] 파일 저장소에 첨부파일 저장
     * @param multipartFile : 컨트롤러에서 전달받은 MultipartFile 객체
     * @return 파일에 접근할 수 있는 파일 저장소의 URL
     * @throws RuntimeException
     */
    String saveMultipartFile(MultipartFile multipartFile) throws RuntimeException;

    /**
     * 사용자 테이블 연동 예시
     * <pre>
     * {@code
     *
     * List<String> attachmentUrls = List.of("htttps://~~", "https://~~");
     * UserDto userDto = ~~;
     * attachmentService.saveMultipartFile(
     *      userDto.getUserId(),
     *      userDto.getUserId(),
     *      AttachmentTableName.USER,
     *      attachmentUrls
     * );
     * }
     * </pre>
     *
     * <br/>
     * 상품 테이블 연동 예시
     * <pre>
     * {@code
     *
     * List<String> attachmentUrls = List.of("htttps://~~", "https://~~");
     * UserDto userDto = ~~;
     * ProductDto productDto = ~~;
     * attachmentService.saveMultipartFile(
     *      userDto.getUserId(),
     *      productDto.getProductId(),
     *      AttachmentTableName.PRODUCT,
     *      attachmentUrls
     * );
     * }
     * </pre>
     *
     * <br>
     *
     * [CREATE] DB 테이블에 첨부파일 정보 저장
     * @param userId : 사용자 ID
     * @param tableId : 테이블의 기본키. ex) qna 테이블의 경우 qna_id
     * @param tableName : 테이블 이름. ex) qna 테이블의 경우 AttachmentTableName.QNA
     * @param attachmentUrls : 첨부파일 URL 리스트. null 또는 empty 리스트 가능 -> 이 경우 아무 동작도 하지 않음
     * @throws RuntimeException
     * @see AttachmentTableName
     */
    void createAttachments(String userId, String tableId, AttachmentTableName tableName, List<String> attachmentUrls) throws RuntimeException;

    /**
     * [READ] 첨부파일 조회
     * @param tableId : 테이블의 기본키. ex) qna 테이블의 경우 qna_id
     * @param tableName : 테이블 이름. ex) qna 테이블의 경우 AttachmentTableName.QNA
     * @return List<AttachmentDto> : 첨부파일 리스트
     * @throws RuntimeException
     */
    List<AttachmentDto> findAttachmentsBy(String tableId, AttachmentTableName tableName) throws RuntimeException;

    /**
     * [DELETE] 첨부파일 여러건 삭제
     * @param userId : 사용자 ID
     * @param tableId : 테이블의 기본키. ex) qna 테이블의 경우 qna_id
     * @param tableName : 테이블 이름. ex) qna 테이블의 경우 AttachmentTableName.QNA
     * @throws RuntimeException
     */
    void deleteAttachmentsBy(String userId, String tableId, AttachmentTableName tableName) throws RuntimeException;
}
