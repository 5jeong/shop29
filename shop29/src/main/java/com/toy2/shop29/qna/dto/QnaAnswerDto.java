package com.toy2.shop29.qna.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class QnaAnswerDto {
    private String admin_id;
    private Integer qna_id;
    private String content;

    // 시스템 컬럼
    private LocalDateTime created_time;
    private String created_id;
    private LocalDateTime updated_time;
    private String updated_id;
}
