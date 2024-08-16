package com.toy2.shop29.qna.domain.response;

import com.toy2.shop29.qna.domain.QnaDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class QnaAdminResponse {
    // 문의유형
    private String qnaTypeId;
    private String qnaTypeName;

    // 1:1 문의
    private int qnaId;
    private String title;
    private LocalDateTime createdTime;

    // 유저 정보
    private String userId;
    private String userName;

    // 문의답변
    private String adminId;
    private String answerContent;
    private LocalDateTime answerCreatedTime;

    public static QnaAdminResponse of(QnaDto qnaDto) {
        QnaAdminResponse qnaAdminResponse = new QnaAdminResponse();

        // 문의유형
        qnaAdminResponse.qnaTypeId = qnaDto.getQnaTypeId();
        qnaAdminResponse.qnaTypeName = qnaDto.getQnaType().getName();
        // 1:1 문의
        qnaAdminResponse.qnaId = qnaDto.getQnaId();
        qnaAdminResponse.title = qnaDto.getTitle();
        qnaAdminResponse.createdTime = qnaDto.getCreatedTime();
        // 유저 정보
        qnaAdminResponse.userId = qnaDto.getUserId();
        qnaAdminResponse.userName = qnaDto.getUser().getUserName();

        // 문의답변
        if(qnaDto.getQnaAnswer() != null) {
            qnaAdminResponse.adminId = qnaDto.getQnaAnswer().getAdminId();
            qnaAdminResponse.answerContent = qnaDto.getQnaAnswer().getContent();
            qnaAdminResponse.answerCreatedTime = qnaDto.getQnaAnswer().getCreatedTime();
        }
        return qnaAdminResponse;
    }
}