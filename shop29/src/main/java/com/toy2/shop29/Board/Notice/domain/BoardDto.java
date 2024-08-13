package com.toy2.shop29.Board.Notice.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.AllArgsConstructor;
import java.util.Date;
// Lombok으로 애너테이션 사용
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class BoardDto {
    private Integer noticeId;
    private String noticeTitle;
    private String noticeContent;
    private Integer noticeNumber=0;
    private String noticeCreatorId;
    private String noticeModifierId;
    private Date noticeCreationTime;
    private Date noticeModificationTime;
    private boolean topFixed;
    private int topFixedPriority;

    //기본 생성자
    public BoardDto(){

    }

    public BoardDto(Integer noticeId, String noticeTitle, String noticeContent, String noticeCreatorId) {
        this.noticeId = noticeId;
        this.noticeTitle = noticeTitle;
        this.noticeContent = noticeContent;
        this.noticeCreatorId = noticeCreatorId;
        this.noticeCreationTime = new Date(); // 현재 날짜로 초기화
        this.noticeModificationTime = new Date(); // 현재 날짜로 초기화


    }


}

