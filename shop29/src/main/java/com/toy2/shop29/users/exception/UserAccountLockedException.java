package com.toy2.shop29.users.exception;

public class UserAccountLockedException extends RuntimeException{
    public UserAccountLockedException() {
        super();
    }

    public UserAccountLockedException(String message) {
        super(message);
    }
}
