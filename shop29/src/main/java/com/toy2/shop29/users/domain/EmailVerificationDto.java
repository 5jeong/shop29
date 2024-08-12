package com.toy2.shop29.users.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EmailVerificationDto {
    private final String subject = "회원가입 이메일 인증입니다.";
    private final String body = "다음 인증 코드를 사용해 주세요 : ";
    private String emailRecipent;
    private String verificationCode;
}
