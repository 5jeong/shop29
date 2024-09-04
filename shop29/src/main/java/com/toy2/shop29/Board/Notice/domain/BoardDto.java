package com.toy2.shop29.Board.Notice.domain;

import lombok.*;

import java.util.Date;
// Lombok으로 애너테이션 사용
@Data
//@Getter, @Setter, @ToString, @EqualsAndHashCode, @AllArgsConstructor(모든 멤버변수 초기화하는 생성자 생성)
// -> @Data 어노테이션 안에 다 포함됨.

public class BoardDto {
    private Integer noticeId;
    private String noticeTitle;
    private String noticeContent;
    private String noticeCreatorId;
    private String noticeModifierId;
    private Date noticeCreationTime;
    private Date noticeModificationTime;
    private boolean topFixed;
    private int topFixedPriority;

    //기본 생성자
    public BoardDto(){

    }

    public BoardDto( String noticeTitle, String noticeContent, String noticeCreatorId, Date noticeCreationTime) {
//        this.noticeId = noticeId;   //noitcId는 auto_increase라서 생략
        this.noticeTitle = noticeTitle;
        this.noticeContent = noticeContent;
        this.noticeCreatorId = noticeCreatorId;
        this.noticeCreationTime = new Date(); // 현재 날짜로 초기화



    }


}

