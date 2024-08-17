package com.toy2.shop29;

import com.toy2.shop29.users.domain.UserDto;
import com.toy2.shop29.users.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@RequiredArgsConstructor
@Controller
public class HomeController {

    private final UserService userService;
    @GetMapping("/")
    public String home(@SessionAttribute(name = "loginUser", required = false) String userId, Model model) {

        System.out.println("세션id = " + userId);
        //세션에 회원이 없으면 로그인 홈
        if (userId == null) {
            model.addAttribute("isLogin", false);
        } else {
            model.addAttribute("isLogin", true);
            UserDto user = userService.findById(userId);
            model.addAttribute("user", user);
        }
        return "home";
    }
}
