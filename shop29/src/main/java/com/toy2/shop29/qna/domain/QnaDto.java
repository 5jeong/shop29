package com.toy2.shop29.qna.domain;

import com.toy2.shop29.users.domain.UserDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QnaDto {
    private Integer qnaId;
    private String userId;
    private String qnaTypeId;
    private String title;
    private String content;
    private String orderId;
    private Integer productId;

    private boolean isDeleted;
    private LocalDateTime deletedTime;

    // System columns
    private LocalDateTime createdTime;
    private String createdId;
    private LocalDateTime updatedTime;
    private String updatedId;

    // JOIN
    private QnaTypeDto qnaType;
    private UserDto user;
    private QnaAnswerDto qnaAnswer;
    private List<AttachmentDto> attachments;
}