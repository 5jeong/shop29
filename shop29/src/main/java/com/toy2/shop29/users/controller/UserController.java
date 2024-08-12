package com.toy2.shop29.users.controller;

import com.toy2.shop29.users.domain.UserRegisterDto;
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

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserServiceImpl userService;
    private final EmailVerificationService emailVerificationService;

    @GetMapping("/signup")
    public String addForm(@ModelAttribute UserRegisterDto userRegisterDto) {
        return "user/addUserForm";
    }

    @PostMapping("/signup")
    public String addUser(@Validated @ModelAttribute UserRegisterDto userRegisterDto,
                          BindingResult bindingResult, Model model, HttpSession session) {

        userService.validateDuplicatedInfo(userRegisterDto, bindingResult);

        if (bindingResult.hasErrors()) {
            log.info("회원가입 에러 : {}", bindingResult);
//            model.addAttribute("verificationStatus",emailVerificationService.isEmail(userRegisterDto.getEmail()));
            return "user/addUserForm";
        }

        log.info("회원정보 :{}", userRegisterDto);
        userService.insertUser(userRegisterDto);
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
    @PostMapping("/sendVerificationCode")
    public ResponseEntity<Map<String, Boolean>> sendVerificationCode(@RequestBody Map<String, String> requestData) {
        String email = requestData.get("email");
        emailVerificationService.sendVerificationCode(email);
        Map<String, Boolean> response = new HashMap<>();
        response.put("success", true);
        return ResponseEntity.ok(response);
    }


    @ResponseBody
    @PostMapping("/verifyCode")
    public ResponseEntity<String> verifyCode(@RequestParam(name = "email") String email,
                                             @RequestParam("code") String code, HttpSession session) {
//        System.out.println("이메일 : " + email);
//        System.out.println("코드 : " + code);
        boolean isVerified = emailVerificationService.verifyCode(email, code);
        if (isVerified) {
            session.setAttribute("emailVerifed", true);
            session.setAttribute("verifiedEmail", email);
//            System.out.println("Verification 성공.");
            return ResponseEntity.ok("이메일 인증 성공.");
        } else {
//            System.out.println("Verification 실패.");
            return ResponseEntity.badRequest().body("이메일 인증 실패. 다시 시도해주세요.");
        }
    }


}
