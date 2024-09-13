package com.toy2.shop29.qna.domain;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AttachmentDto {
    private Integer attachmentId; // 첨부파일 ID
    private String tableId; // 테이블의 기본키. ex) qna 테이블의 경우 qna_id
    private AttachmentTableName tableName; // 테이블 이름. ex) qna 테이블의 경우 AttachmentTableName.QNA
    private String fileName; // 파일명
    private String filePath; // 파일저장소 內 파일에 접근할 수 있는 URL
    private Integer width; // 이미지의 가로 길이
    private Integer height; // 이미지의 세로 길이
    private long size; // 파일 크기
    private String extension; // 파일 확장자
    private Boolean isActive; // 활성화 여부

    // 시스템 컬럼
    private LocalDateTime createdTime;
    private String createdId;
    private LocalDateTime updatedTime;
    private String updatedId;
}
