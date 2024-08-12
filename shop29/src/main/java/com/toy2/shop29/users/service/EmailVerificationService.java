package com.toy2.shop29.users.service;

import com.toy2.shop29.users.domain.EmailVerificationDto;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final Map<String, String> verificationCodes = new ConcurrentHashMap<>();
    //    private final Map<EmailDto,String> emailVerifyStatus = new ConcurrentHashMap<>();
    private final EmailService emailService;
    private final ScheduledExecutorService schedular = Executors.newScheduledThreadPool(1);

    public void sendVerificationCode(String email) {
        String verificationCode = generateVerificationCode();
        // EmailDto 생성
        EmailVerificationDto emailDto = EmailVerificationDto.builder()
                .emailRecipent(email)
                .verificationCode(verificationCode)
                .build();

        System.out.println(email);

        verificationCodes.put(email, verificationCode);

        // 10분 후에 a자동으로 인증코드 삭제
        schedular.schedule(() -> verificationCodes.remove(email), 10, TimeUnit.MINUTES);

        emailService.sendVerificationEmail(emailDto);
    }

    public boolean verifyCode(String email, String code) {
        String storedCode = verificationCodes.get(email);
        System.out.println("저장코드" + storedCode);
        System.out.println(verificationCodes);
        if (storedCode != null && storedCode.equals(code)) {
//            verificationCodes.remove(email); // 인증 후 삭제
            return true;
        }
        return false;
    }

    private String generateVerificationCode() {
        return UUID.randomUUID().toString().substring(0, 6);
    }

}
