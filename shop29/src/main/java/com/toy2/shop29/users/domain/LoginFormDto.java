package com.toy2.shop29.users.domain;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginFormDto {
    @NotEmpty(message = "아이디를 입력해주세요.")
    private String userId;
    @NotEmpty(message = "비밀번호를 입력해주세요.")
    private String password;
}
