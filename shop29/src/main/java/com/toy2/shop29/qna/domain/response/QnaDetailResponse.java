package com.toy2.shop29.qna.domain.response;

import com.toy2.shop29.qna.domain.QnaDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class QnaDetailResponse {
    // 문의유형
    private String qnaTypeId;
    private String qnaTypeName;
    private Boolean qnaTypeIsActive;

    // 부모 문의유형
    private String parentQnaTypeId;
    private String parentQnaTypeName;
    private Boolean parentQnaTypeIsActive;

    // 주문ID
    private int orderId;

    // 상품ID
    private int productId;

    // 1:1문의
    private int qnaId;
    private String title;
    private LocalDateTime createdTime;

    // 첨부파일
    private List<String> attachmentPaths;

    // 유저 정보
    private String userId;
    private String userName;

    // 문의답변
    private String adminId;
    private String answerContent;
    private LocalDateTime answerCreatedTime;

    public static QnaDetailResponse of(QnaDto qnaDto){
        QnaDetailResponse res = new QnaDetailResponse();
        // 문의유형
        res.setQnaTypeId(qnaDto.getQnaTypeId());
        res.setQnaTypeName(qnaDto.getQnaType().getName());
        res.setQnaTypeIsActive(qnaDto.getQnaType().isActive());

        // 부모 문의유형
        res.setParentQnaTypeId(qnaDto.getQnaType().getParentQnaType().getParentQnaTypeId());
        res.setParentQnaTypeName(qnaDto.getQnaType().getParentQnaType().getName());
        res.setParentQnaTypeIsActive(qnaDto.getQnaType().getParentQnaType().isActive());

        // 주문ID 와 상품ID
        res.setOrderId(qnaDto.getOrderId());
        res.setProductId(qnaDto.getProductId());

        // 1:1문의
        res.setQnaId(qnaDto.getQnaId());
        res.setTitle(qnaDto.getTitle());
        res.setCreatedTime(qnaDto.getCreatedTime());

        // 첨부파일
        res.setAttachmentPaths(qnaDto.getAttachments().stream()
                .map(attachment -> attachment.getFilePath())
                .toList());

        // 유저 정보
        res.setUserId(qnaDto.getUserId());
        res.setUserName(qnaDto.getUser().getUserName());

        // 문의답변
        if(qnaDto.getQnaAnswer() != null){
            res.setAdminId(qnaDto.getQnaAnswer().getAdminId());
            res.setAnswerContent(qnaDto.getQnaAnswer().getContent());
            res.setAnswerCreatedTime(qnaDto.getQnaAnswer().getCreatedTime());
        }
        return res;
    }
}