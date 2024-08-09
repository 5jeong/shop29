package com.toy2.shop29.qna.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ParentQnaTypeDto {
    @NotNull
    private String parentQnaTypeId; // 부모 문의유형 ID

    @NotNull
    private String name; // 부모 문의유형 이름

    @NotNull
    private String description; // 부모 문의유형 설명
    private boolean isActive; // 사용여부

    // System columns
    private LocalDateTime createdTime;

    @NotNull
    private String createdId;

    private LocalDateTime updatedTime;

    @NotNull
    private String updatedId;
}