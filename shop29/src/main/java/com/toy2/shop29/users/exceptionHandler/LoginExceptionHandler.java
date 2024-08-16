package com.toy2.shop29.users.exceptionHandler;

import com.toy2.shop29.users.exception.loginException.IncorrectPasswordException;
import com.toy2.shop29.users.exception.loginException.UserAccountLockedException;
import com.toy2.shop29.users.exception.loginException.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice("com.toy2.shop29.users.controller")
public class LoginExceptionHandler {

    @ExceptionHandler({UserNotFoundException.class, UserAccountLockedException.class, IncorrectPasswordException.class})
    public String handleLoginExceptions(RuntimeException ex, Model model, HttpServletRequest request) {
        model.addAttribute("loginError", ex.getMessage());
//        request.getAttribute("loginForm");
//        model.addAttribute("loginForm", new LoginFormDto());
        model.addAttribute("loginForm", request.getAttribute("loginForm"));
        log.error("[exceptionHandler] ex : ", ex);
        return "login/loginForm";
    }

}

