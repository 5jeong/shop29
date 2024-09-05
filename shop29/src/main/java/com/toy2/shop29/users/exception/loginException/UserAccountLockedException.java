package com.toy2.shop29.users.exception.loginException;

import org.springframework.security.core.AuthenticationException;

public class UserAccountLockedException extends AuthenticationException {

    public UserAccountLockedException(String message) {
        super(message);
    }
}
