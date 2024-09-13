package com.toy2.shop29.users.controller;

import com.toy2.shop29.users.service.email.EmailVerificationService;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
public class EmailController {

    private final EmailVerificationService emailVerificationService;

    @PostMapping("/sendVerificationCode")
    public ResponseEntity<Map<String, Boolean>> sendVerificationCode(@RequestParam String email) {
        emailVerificationService.generateVerificationCode(email);
        Map<String, Boolean> response = new HashMap<>();
        response.put("success", true);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/sendTempPassword")
    public ResponseEntity<Map<String, Boolean>> sendTempPassword(@RequestParam String userId,
                                                                 @RequestParam String email) {
        emailVerificationService.generateTempPassword(userId, email);
        Map<String, Boolean> response = new HashMap<>();
        response.put("success", true);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verifyCode")
    public ResponseEntity<String> verifyCode(@RequestParam(name = "email") String email,
                                             @RequestParam("code") String code) {
        boolean isVerified = emailVerificationService.verifyCode(email, code);
        if (isVerified) {
            return ResponseEntity.ok("이메일 인증 성공.");
        } else {
            return ResponseEntity.badRequest().body("이메일 인증 실패. 다시 시도해주세요.");
        }
    }
}
