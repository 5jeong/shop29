package com.toy2.shop29.qna.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class QnaDto {
    private Integer qnaId;
    private String userId;
    private String qnaTypeId;
    private String title;
    private String content;
    private Integer orderId;
    private Integer productId;
    private Boolean isDeleted;
    private LocalDateTime deletedTime;

    // System columns
    private LocalDateTime createdTime;
    private String createdId;
    private LocalDateTime updatedTime;
    private String updatedId;
}