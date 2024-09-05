package com.toy2.shop29.users.controller;

import com.toy2.shop29.users.domain.LoginFormDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    @GetMapping("/login")
    public String loginForm(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "exception", required = false) String exception, Model model,
                            HttpServletRequest request) {

        // 로그인 실패가 아닌경우에만 redirectUrl 저장
        if(error == null){
            String redirectUrl = request.getHeader("Referer");
            if (redirectUrl != null) {
                request.getSession().setAttribute("redirectUrl", redirectUrl);
            }
        }
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);
        model.addAttribute("loginForm", new LoginFormDto());

        return "login/loginForm";
    }

}
