package com.toy2.shop29.users.domain;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginFormDto {
    @NotEmpty
    private String userId;
    @NotEmpty
    private String password;
}
