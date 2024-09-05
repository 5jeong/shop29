package com.toy2.shop29;

import com.toy2.shop29.users.domain.UserDto;
import com.toy2.shop29.users.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model, @AuthenticationPrincipal UserDto user) {
        if(user != null){
            model.addAttribute("user",user);
        }
        return "home";
    }
}
