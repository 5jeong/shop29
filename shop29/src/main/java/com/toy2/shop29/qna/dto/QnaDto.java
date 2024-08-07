package com.toy2.shop29.qna.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class QnaDto {
    private Integer qna_id;
    private String user_id;
    private String qna_type_id;
    private String title;
    private String content;
    private Integer order_id;
    private Integer product_id;
    private Boolean is_deleted;
    private LocalDateTime deleted_time;

    // 시스템 컬럼
    private LocalDateTime created_time;
    private String created_id;
    private LocalDateTime updated_time;
    private String updated_id;
}
