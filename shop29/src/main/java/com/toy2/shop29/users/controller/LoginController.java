package com.toy2.shop29.users.controller;

import com.toy2.shop29.users.domain.LoginFormDto;
import com.toy2.shop29.users.domain.UserDto;
import com.toy2.shop29.users.service.login.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute(name = "loginForm") LoginFormDto loginFormDto) {
        return "login/loginForm";
    }

    @PostMapping("/login")
    public String login(@Validated @ModelAttribute(name = "loginForm") LoginFormDto loginFormDto,
                        BindingResult bindingResult, HttpServletRequest request,
                        @RequestParam(name = "redirectURI", defaultValue = "/") String redirectURI) {
        if (bindingResult.hasErrors()) {
            log.info("bindingResult : {} ", bindingResult);
            return "login/loginForm";
        }

        // 예외가 발생하면 해당 예외는 LoginExceptionHandler에서 처리됨
        request.setAttribute("loginForm", loginFormDto);
        UserDto loginUser = loginService.loginCheck(loginFormDto.getUserId(), loginFormDto.getPassword());
        log.info("login : {}", loginUser);

        HttpSession session = request.getSession();
        session.setAttribute("loginUser", loginUser);
        return "redirect:" + redirectURI;
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {

        HttpSession session = request.getSession();
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }

}
