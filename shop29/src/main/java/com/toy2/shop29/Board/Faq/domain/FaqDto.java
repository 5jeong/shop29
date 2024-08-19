package com.toy2.shop29.Board.Faq.domain;

import lombok.*;

import java.util.Date;
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
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
