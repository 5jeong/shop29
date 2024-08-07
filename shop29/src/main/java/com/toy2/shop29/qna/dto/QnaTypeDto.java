package com.toy2.shop29.qna.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class QnaTypeDto {
    private String qna_type_id;
    private String parent_qna_type_id;
    private String name;
    private String description;
    private Boolean is_order_id_active;
    private Boolean is_product_id_active;
    private Boolean is_active;

    // 시스템 컬럼
    private LocalDateTime created_time;
    private String created_id;
    private LocalDateTime updated_time;
    private String updated_id;
}
