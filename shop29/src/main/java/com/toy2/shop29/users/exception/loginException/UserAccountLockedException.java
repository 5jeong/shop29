package com.toy2.shop29.users.exception.loginException;

public class UserAccountLockedException extends RuntimeException{

    public UserAccountLockedException(String message) {
        super(message);
    }
}
