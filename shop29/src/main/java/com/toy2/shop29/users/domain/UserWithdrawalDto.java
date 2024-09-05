package com.toy2.shop29.users.domain;

import lombok.Data;

@Data
public class UserWithdrawalDto {
    private String reasonPrimary;
    private String reasonSecondary;
}
