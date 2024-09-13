package com.toy2.shop29.users.exception.loginException;

import org.springframework.security.core.AuthenticationException;

public class EmptyFieldException extends AuthenticationException {
    public EmptyFieldException(String msg) {
        super(msg);
    }
}
