package com.toy2.shop29.qna.domain;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AttachmentDto {
    private Integer attachmentId;
    private String tableId;
    private AttachmentTableName tableName; // 'qna' 로 제한
    private String fileName;
    private String filePath;
    private Integer width;
    private Integer height;
    private long size;
    private String extension;
    private Boolean isActive;

    // 시스템 컬럼
    private LocalDateTime createdTime;
    private String createdId;
    private LocalDateTime updatedTime;
    private String updatedId;
}
