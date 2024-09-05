package com.toy2.shop29.qna.domain;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QnaAnswerDto {
    private Integer qnaAnswerId;
    private String adminId;
    private Integer qnaId;
    private String content;

    private boolean isDeleted;
    private LocalDateTime deletedTime;
    private String deletedId;

    // System columns
    private LocalDateTime createdTime;
    private String createdId;
    private LocalDateTime updatedTime;
    private String updatedId;
}