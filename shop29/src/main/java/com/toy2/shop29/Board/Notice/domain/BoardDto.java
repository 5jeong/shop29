package com.toy2.shop29.Board.Notice.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.Objects;

@Getter @Setter //generate 사용없이 lombok으로 애너테이션으로 처리 가능
@ToString //lombok으로 애너테이션
public class BoardDto {
    private Integer notice_number;
    private String notice_title;
    private String notice_content;
    private String notice_creator_id;
    private String notice_modifier_id;
    private Date notice_creation_time;
    private Date notice_modification_time;
    private boolean top_fixed;
    private int top_fixed_priority;


    //generate -> equals() and hashCode()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardDto boardDto = (BoardDto) o;
        return top_fixed == boardDto.top_fixed && top_fixed_priority == boardDto.top_fixed_priority && Objects.equals(notice_number, boardDto.notice_number) && Objects.equals(notice_title, boardDto.notice_title) && Objects.equals(notice_content, boardDto.notice_content) && Objects.equals(notice_creator_id, boardDto.notice_creator_id) && Objects.equals(notice_modifier_id, boardDto.notice_modifier_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(notice_number, notice_title, notice_content, notice_creator_id, notice_modifier_id, top_fixed, top_fixed_priority);
    }


    // genereate -> Constructor
    public BoardDto() {
    }

    public BoardDto(String notice_title, String notice_content, String notice_creator_id) {
        this.notice_title = notice_title;
        this.notice_content = notice_content;
        this.notice_creator_id = notice_creator_id;
    }


}
