package com.toy2.shop29.users.controller;

import com.toy2.shop29.users.domain.UserContext;
import com.toy2.shop29.users.domain.UserDto;
import com.toy2.shop29.users.domain.UserRegisterDto;
import com.toy2.shop29.users.domain.UserUpdateDto;
import com.toy2.shop29.users.domain.UserWithdrawalDto;
import com.toy2.shop29.users.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/signup")
    public String addForm(@ModelAttribute UserRegisterDto userRegisterDto) {
        return "user/addUserForm";
    }

    @PostMapping("/signup")
    public String addUser(@Validated @ModelAttribute(name = "userRegisterDto") UserRegisterDto userRegisterDto,
                          BindingResult bindingResult) {

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
    public String updateUser(Model model, @AuthenticationPrincipal UserContext userContext) {
        UserDto userDto = userContext.getUserDto();
        UserDto user = userService.findById(userDto.getUserId());
        model.addAttribute("userUpdateDto", user);
        return "user/editUserForm"; // 수정 폼 페이지로 이동
    }

    @PostMapping("/update")
    public String updateUser(@Validated() @ModelAttribute(name = "userUpdateDto") UserUpdateDto userUpdateDto,
                             BindingResult bindingResult,
                             @AuthenticationPrincipal UserContext userContext) {
        if (bindingResult.hasErrors()) {
            log.info("회원 수정에러 : {}", bindingResult);
            return "user/editUserForm";
        }

        // 로그인된 사용자 정보
        String userId = userContext.getUserDto().getUserId();
        userService.updateUser(userId, userUpdateDto);
        return "redirect:/";
    }

    @ResponseBody
    @GetMapping("/checkUserId")
    public ResponseEntity<Map<String, Boolean>> checkUserId(@RequestParam(name = "userId") String userId) {
        boolean exists = userService.isUserIdDuplicated(userId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @ResponseBody
    @GetMapping("/checkUserEmail")
    public ResponseEntity<Map<String, Boolean>> checkUserEmail(@RequestParam(name = "userEmail") String userEmail) {
        boolean exists = userService.isEmailDuplicated(userEmail);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @ResponseBody
    @GetMapping("/checkUserPhoneNumber")
    public ResponseEntity<Map<String, Boolean>> checkPhoneNumber(
            @RequestParam(name = "phoneNumber") String phoneNumber) {
        boolean exists = userService.isPhoneNumberDuplicated(phoneNumber);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/findId")
    public String findUserId() {
        return "user/findUserIdForm";
    }

    @PostMapping("/findIdResult")
    public String findUserId(@RequestParam String email, Model model) {
        String userId = userService.findByEmail(email);
        model.addAttribute("userId", userId);
        return "user/findIdResult";
    }

    @GetMapping("/findPassword")
    public String findPassword() {
        return "user/findPasswordForm";
    }


    @GetMapping("/delete")
    public String delete(@ModelAttribute UserWithdrawalDto userWithdrawalDto) {
        return "user/withdrawalDto";
    }

    @PostMapping("/delete")
    public String delete(@ModelAttribute(name = "userWithdrawalDto") UserWithdrawalDto userWithdrawalDto,
                         @AuthenticationPrincipal UserContext userContext, HttpServletRequest request) {
        UserDto user = userContext.getUserDto();
        userService.insertWithdrawalUser(user.getUserId(), userWithdrawalDto);
        HttpSession session = request.getSession();
        session.invalidate();
        SecurityContextHolder.clearContext();
        return "redirect:/";
    }

    @GetMapping("/mypage")
    public String mypage(@AuthenticationPrincipal UserContext userContext, Model model) {
        model.addAttribute("user", userContext.getUserDto());
        return "user/mypage";
    }
}
