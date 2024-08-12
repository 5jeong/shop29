package com.toy2.shop29.users.exceptionHandler;

import com.toy2.shop29.users.exception.IncorrectPasswordException;
import com.toy2.shop29.users.exception.UserAccountLockedException;
import com.toy2.shop29.users.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice("com.toy2.shop29.users.controller")
public class LoginExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFoundException(UserNotFoundException ex, Model model) {
        log.error("[exceptionHandler] ex : ", ex);
        model.addAttribute("loginError", ex.getMessage());
        return "login/loginForm";
    }

    @ExceptionHandler(UserAccountLockedException.class)
    public String handleUserAccountLockedException(UserAccountLockedException ex, Model model) {
        log.error("[exceptionHandler] ex : ", ex);
        model.addAttribute("loginError", ex.getMessage());
        return "login/loginForm";
    }

    @ExceptionHandler(IncorrectPasswordException.class)
    public String handleIncorrectPasswordException(IncorrectPasswordException ex, Model model) {
        log.error("[exceptionHandler] ex : ", ex);
        model.addAttribute("loginError", "비밀번호가 올바르지 않습니다. 다시 시도해주세요.");
        return "login/loginForm";
    }
}

