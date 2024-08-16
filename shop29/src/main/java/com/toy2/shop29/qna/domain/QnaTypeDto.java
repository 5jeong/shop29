package com.toy2.shop29.qna.domain;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QnaTypeDto {
    @NotNull
    private String qnaTypeId;
    @NotNull
    private String parentQnaTypeId;
    @NotNull
    private String name;
    @NotNull
    private String description;
    private boolean isOrderIdActive;
    private boolean isProductIdActive;
    private boolean isActive;

    // System columns
    private LocalDateTime createdTime;
    @NotNull
    private String createdId;
    private LocalDateTime updatedTime;
    @NotNull
    private String updatedId;

    // Join
    private ParentQnaTypeDto parentQnaType;
}