package com.toy2.shop29.users.controller;

import com.toy2.shop29.users.domain.UserDto;
import com.toy2.shop29.users.domain.UserRegisterDto;
import com.toy2.shop29.users.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserServiceImpl userService;

    @GetMapping("/signup")
    public String addForm(@ModelAttribute UserRegisterDto userRegisterDto){
        return "user/addUserForm";
    }

    @PostMapping("/signup")
    public String addUser(@Validated @ModelAttribute UserRegisterDto userRegisterDto, BindingResult bindingResult){

        // 회원 id중복 검증
        if(userService.isUserIdDuplicated(userRegisterDto.getUserId())){
            bindingResult.rejectValue("userId","duplicate");
            return "user/addUserForm";
        }

        if(bindingResult.hasErrors()){
            log.info("회원가입 에러 : {}",bindingResult);
            return "user/addUserForm";
        }

        log.info("회원정보 :{}",userRegisterDto);
        userService.insertUser(userRegisterDto);
        return "redirect:/";
    }

}
