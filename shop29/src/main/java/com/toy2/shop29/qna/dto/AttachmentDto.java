package com.toy2.shop29.qna.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class AttachmentDto {
    private Integer attachment_id;
    private Integer qna_id;
    private String file_name;
    private String file_path;
    private Integer width;
    private Integer height;
    private Integer size;
    private String extension;
    private Integer is_active;

    // 시스템 컬럼
    private LocalDateTime created_time;
    private String created_id;
    private LocalDateTime updated_time;
    private String updated_id;
}
