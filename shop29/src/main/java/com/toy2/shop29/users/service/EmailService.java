package com.toy2.shop29.users.service;

import com.toy2.shop29.users.domain.EmailVerificationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    // SMTP를 통해 이메일 전송할 수 있도록 지원,
    // 메일 객체 생성, 메일 전송 등의 기능을 제공
    private final JavaMailSender mailSender;

    // 이메일을 발송할 발신자 주소
    @Value("${spring.mail.username")
    private String emailSender;



    // 이메일 인증번호 전송
    public void sendVerificationEmail(EmailVerificationDto emailDto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailSender); // 발신자 이메일 설정
        message.setTo(emailDto.getEmailRecipent()); // 수신자 이메일 설정
        message.setSubject(emailDto.getSubject()); // 이메일 제목 설정
        message.setText(emailDto.getBody() + "\n인증 코드: " + emailDto.getVerificationCode()); // 이메일 본문 설정

        mailSender.send(message); // 이메일 전송
    }
}

