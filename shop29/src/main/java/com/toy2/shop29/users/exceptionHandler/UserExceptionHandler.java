package com.toy2.shop29.users.exceptionHandler;

import com.toy2.shop29.users.exception.loginException.IncorrectPasswordException;
import com.toy2.shop29.users.exception.loginException.UserAccountLockedException;
import com.toy2.shop29.users.exception.loginException.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@ControllerAdvice("com.toy2.shop29.users.controller")
public class UserExceptionHandler {

    @ExceptionHandler({UserNotFoundException.class, UserAccountLockedException.class, IncorrectPasswordException.class})
    public String handleLoginExceptions(RuntimeException ex, RedirectAttributes rttr, Model model,
                                        HttpServletRequest request) {
        // 예외 메시지를 모델에 추가하여 뷰에 전달

        rttr.addFlashAttribute("loginError", ex.getMessage());
        rttr.addFlashAttribute("loginForm", request.getAttribute("loginForm"));
        log.error("[exceptionHandler] ex : ", request.getAttribute("loginForm"));
        return "redirect:/login";
    }

//    @ExceptionHandler({UserIdDuplicateException.class, EmailDuplicateException.class,
//            PhoneNumberDuplicateException.class})
//    public String handleDuplicateExceptions(RuntimeException ex, Model model) {
//        // 예외 메시지를 모델에 추가하여 뷰에 전달
//        model.addAttribute("errorMessage", ex.getMessage());
//        return "user/addUserForm"; // "user/duplicateError"는 에러를 보여줄 템플릿 경로
//    }
}

