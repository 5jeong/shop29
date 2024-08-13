package com.toy2.shop29.users.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EmailDto {
    // 이메일 제목
    private String subject;
    // 이메일 본문
    private String body;
    // 이메일 수신자
    private String emailRecipent;
    // 이메일 코드(인증번호, 임시비밀번호)
    private String code;
}
