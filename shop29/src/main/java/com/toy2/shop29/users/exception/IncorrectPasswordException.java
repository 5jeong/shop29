package com.toy2.shop29.users.exception;

public class IncorrectPasswordException extends RuntimeException{
    public IncorrectPasswordException() {
        super();
    }

    public IncorrectPasswordException(String message) {
        super(message);
    }

}
