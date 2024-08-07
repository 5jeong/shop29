package com.toy2.shop29.qna.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ParentQnaTypeDto {
    private String parent_qna_type_id;
    private String name;
    private String description;
    private Boolean is_active;

    // 시스템 컬럼
    private LocalDateTime created_time;
    private String created_id;
    private LocalDateTime updated_time;
    private String updated_id;
}
