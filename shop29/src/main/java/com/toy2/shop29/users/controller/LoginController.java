package com.toy2.shop29.users.controller;

import com.toy2.shop29.users.domain.LoginFormDto;
import com.toy2.shop29.users.domain.UserDto;
import com.toy2.shop29.users.exception.IncorrectPasswordException;
import com.toy2.shop29.users.exception.UserAccountLockedException;
import com.toy2.shop29.users.exception.UserNotFoundException;
import com.toy2.shop29.users.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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
                        BindingResult bindingResult, HttpServletRequest request, Model model) {
        if (bindingResult.hasErrors()) {
            log.info("bindingResult : {} ",bindingResult);
            return "login/loginForm";
        }

        try {
            UserDto loginUser = loginService.loginCheck(loginFormDto.getUserId(), loginFormDto.getPassword());
            log.info("login? {}", loginUser);
            HttpSession session = request.getSession();
            session.setAttribute("loginMember", loginUser);
            return "redirect:/";
        } catch (UserNotFoundException | UserAccountLockedException | IncorrectPasswordException e) {
            model.addAttribute("loginError",e.getMessage());
            return "login/loginForm";
        }
    }

}
