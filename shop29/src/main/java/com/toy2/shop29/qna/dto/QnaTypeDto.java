package com.toy2.shop29.qna.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class QnaTypeDto {
    private String qnaTypeId;
    private String parentQnaTypeId;
    private String name;
    private String description;
    private Boolean isOrderIdActive;
    private Boolean isProductIdActive;
    private Boolean isActive;

    // System columns
    private LocalDateTime createdTime;
    private String createdId;
    private LocalDateTime updatedTime;
    private String updatedId;
}