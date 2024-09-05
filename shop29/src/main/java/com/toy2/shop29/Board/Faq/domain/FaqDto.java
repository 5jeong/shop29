package com.toy2.shop29.Board.Faq.domain;

import lombok.*;

import java.util.Date;
@Data
//@Getter, @Setter, @ToString, @EqualsAndHashCode, @AllArgsConstructor(모든 멤버변수 초기화하는 생성자 생성)
// -> @Data 어노테이션 안에 다 포함됨.
    public class FaqDto {
    private Integer faqId;
    private String faqTypeId;
    private String faqTitle;
    private String faqContent;
    private String faqCreatorId;
    private String faqModifierId;
    private Date faqCreationTime;
    private Date faqModificationTime;
    private Integer faqViewCount;

    //기본 생성자
    public FaqDto() {

    }

    public FaqDto(String faqTitle, String faqContent, String faqCreatorId, String faqTypeId) {
//        this.faqId = faqId;   //faqId는  auto_increase라서 생략
        this.faqTitle = faqTitle;
        this.faqContent = faqContent;
        this.faqCreatorId = faqCreatorId;
        this.faqTypeId=faqTypeId;

    }
}
