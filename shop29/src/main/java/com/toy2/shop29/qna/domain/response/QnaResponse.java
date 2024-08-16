package com.toy2.shop29.qna.domain.response;

import com.toy2.shop29.qna.domain.QnaDto;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class QnaResponse {
    // 문의유형
    private String qnaTypeId;
    private String qnaTypeName;

    // 1:1 문의
    private int qnaId;
    private String title;
    private String content;
    private LocalDateTime createdTime;

    // 문의답변
    private String answerContent;
    private LocalDateTime answerCreatedTime;

    // 첨부파일
    private List<String> attachmentPaths;

    public static QnaResponse of(QnaDto qnaDto){
        QnaResponse res = new QnaResponse();

        // 문의유형
        res.qnaTypeId = qnaDto.getQnaTypeId();
        res.qnaTypeName = qnaDto.getQnaType().getName();

        // 1:1 문의
        res.qnaId = qnaDto.getQnaId();
        res.title = qnaDto.getTitle();
        res.content = qnaDto.getContent();
        res.createdTime = qnaDto.getCreatedTime();

        // 문의답변
        if(qnaDto.getQnaAnswer() != null){
            res.answerContent = qnaDto.getQnaAnswer().getContent();
            res.answerCreatedTime = qnaDto.getQnaAnswer().getCreatedTime();
        }

        // 첨부파일
        res.attachmentPaths = qnaDto.getAttachments().stream()
                .map(attachment -> attachment.getFilePath()).toList();

        return res;
    }
}