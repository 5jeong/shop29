package com.toy2.shop29.qna.domain.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QnaCreateRequest {
    @NotEmpty
    private String qnaTypeId; // 문의유형 ID
    @NotEmpty
    private String title; // 1:1문의 제목
    @NotEmpty
    private String content; // 1:1문의 내용
    private List<String> attachmentNames; // 첨부파일 이름 리스트

    private String orderId; // 주문 ID

    // 상품 ID가 null이 아닐 경우에만 유효성 검사
    @Min(value = 1, message = "상품 ID는 1 이상의 숫자여야 합니다.")
    private Integer productId; // 상품 ID

    @AssertTrue(message = "상품 ID가 존재하는 경우, 1 이상의 숫자여야 합니다.")
    private boolean isValidProductId() {
        return productId == null || productId >= 1;
    }
}