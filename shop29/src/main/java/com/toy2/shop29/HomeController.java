package com.toy2.shop29;

import com.toy2.shop29.users.domain.UserDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller

public class HomeController {
    @GetMapping("/")
    public String home(@SessionAttribute(name = "loginUser", required = false) String userId, Model model) {
        //세션에 회원이 없으면 로그인 홈
        if (userId == null) {
            model.addAttribute("isLogin", false);
        } else {
            model.addAttribute("isLogin", true);
            model.addAttribute("user", userId);
        }
        return "home";
    }
}
