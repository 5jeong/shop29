package com.toy2.shop29.qna.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class UserForQnaDto {
    private String userId; //로그인 ID
    private String userName;  // 회원 이름
    private String userRole; //회원 권한
    private String email;   // 이메일
    private String phoneNumber;  // 전화번호
}