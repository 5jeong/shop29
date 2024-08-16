package com.toy2.shop29.qna.domain.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QnaCreateRequest {
    private String qnaTypeId; // 문의유형 ID
    private String title; // 1:1문의 제목
    private String content; // 1:1문의 내용
    private List<String> attachmentNames; // 첨부파일 이름 리스트
    private Integer orderId; // 주문 ID
    private Integer productId; // 상품 ID
}