package com.toy2.shop29.exception;

import com.toy2.shop29.users.exception.loginException.IncorrectPasswordException;
import com.toy2.shop29.users.exception.loginException.UserAccountLockedException;
import com.toy2.shop29.users.exception.loginException.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleAllExceptions(Exception ex, Model model) {
        ModelAndView modelAndView = new ModelAndView("error/500");
        modelAndView.addObject("message", ex.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handle404(NoHandlerFoundException ex, Model model) {
        model.addAttribute("message", "The page you are looking for does not exist.");
        return "error/404"; // templates/error/404.html을 반환
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ModelAndView handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        ModelAndView mav = new ModelAndView("error/405");
        mav.addObject("error", "HTTP 메서드가 지원되지 않습니다.");
        mav.addObject("message", ex.getMessage());
        return mav;
    }

    @ExceptionHandler({UserNotFoundException.class, UserAccountLockedException.class, IncorrectPasswordException.class})
    public String handleLoginExceptions(RuntimeException ex, RedirectAttributes rttr, Model model,
                                        HttpServletRequest request) {
        // 예외 메시지를 모델에 추가하여 뷰에 전달

        rttr.addFlashAttribute("loginError", ex.getMessage());
        rttr.addFlashAttribute("loginForm", request.getAttribute("loginForm"));
        log.error("[exceptionHandler] ex : ", request.getAttribute("loginForm"));
        return "redirect:/login";
    }
}
