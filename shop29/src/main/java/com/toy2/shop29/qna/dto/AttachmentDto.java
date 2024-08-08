package com.toy2.shop29.qna.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class AttachmentDto {
    private Integer attachmentId;
    private Integer qnaId;
    private String fileName;
    private String filePath;
    private Integer width;
    private Integer height;
    private Integer size;
    private String extension;
    private Integer isActive;

    // 시스템 컬럼
    private LocalDateTime createdTime;
    private String createdId;
    private LocalDateTime updatedTime;
    private String updatedId;
}
