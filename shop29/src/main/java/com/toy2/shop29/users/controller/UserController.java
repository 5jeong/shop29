package com.toy2.shop29.users.controller;

import com.toy2.shop29.users.domain.UserDto;
import com.toy2.shop29.users.domain.UserRegisterDto;
import com.toy2.shop29.users.domain.UserUpdateDto;
import com.toy2.shop29.users.service.EmailVerificationService;
import com.toy2.shop29.users.service.UserServiceImpl;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserServiceImpl userService;

    @GetMapping("/signup")
    public String addForm(@ModelAttribute UserRegisterDto userRegisterDto) {
        return "user/addUserForm";
    }

    @PostMapping("/signup")
    public String addUser(@Validated @ModelAttribute(name = "userRegisterDto") UserRegisterDto userRegisterDto,
                          BindingResult bindingResult, HttpSession session) {

        userService.validateDuplicatedInfo(userRegisterDto, bindingResult);

        if (bindingResult.hasErrors()) {
            log.info("회원가입 에러 : {}", bindingResult);
            return "user/addUserForm";
        }

        log.info("회원정보 :{}", userRegisterDto);
        userService.insertUser(userRegisterDto);
        return "redirect:/";
    }


    @GetMapping("/update")
    public String updateUser(Model model, @SessionAttribute(name = "loginUser", required = false) UserDto loginUser) {
        if (loginUser == null) {
            return "redirect:/login"; // 로그인 페이지로 리다이렉트
        }
        model.addAttribute("loginUser", loginUser);
        return "user/editUserForm"; // 수정 폼 페이지로 이동
    }


    @PostMapping("/update")
    public String updateUser(@Validated() @ModelAttribute(name = "loginUser") UserUpdateDto userUpdateDto,
                             BindingResult bindingResult,
                             @SessionAttribute(name = "loginUser", required = false) UserDto loginUser) {
        if (bindingResult.hasErrors()) {
            log.info("회원가입 에러 : {}", bindingResult);
            return "user/editUserForm";
        }
        userService.updateUser(loginUser.getUserId(), userUpdateDto);
        return "redirect:/";
    }

    @ResponseBody
    @GetMapping("/checkUserId")
    public ResponseEntity<Map<String, Boolean>> checkUserId(@RequestParam(name = "userId") String userId) {
        boolean exists = userService.isUserIdDuplicated(userId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }


    @ResponseBody
    @GetMapping("/checkUserEmail")
    public ResponseEntity<Map<String, Boolean>> checkUserEmail(@RequestParam(name = "userEmail") String userEmail) {
        boolean exists = userService.isEmailDuplicated(userEmail);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }




}
