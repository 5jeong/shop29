package com.toy2.shop29.users.service;

import com.toy2.shop29.users.domain.EmailDto;
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
        EmailDto emailDto = EmailDto.builder()
                .subject("회원가입 이메일 인증입니다.")
                .body("다음 인증 코드를 사용해 주세요 : "+ "\n인증 코드: " + verificationCode)
                .emailRecipent(email)
                .code(verificationCode)
                .build();

//        System.out.println(email);

        verificationCodes.put(email, verificationCode);

        // 10분 후에 자동으로 인증코드 삭제
        schedular.schedule(() -> verificationCodes.remove(email), 10, TimeUnit.MINUTES);

        emailService.sendEmail(emailDto);
    }

    public void sendTempPassword(String email) {
        String tempPassword = generateTempPassword();
        // EmailDto 생성
        EmailDto emailDto = EmailDto.builder()
                .subject("회원가입 이메일 인증입니다.")
                .body("다음 인증 코드를 사용해 주세요 : ")
                .emailRecipent(email)
                .code(tempPassword)
                .build();

//        System.out.println(email);

        verificationCodes.put(email, tempPassword);

        // 10분 후에 자동으로 인증코드 삭제
        schedular.schedule(() -> verificationCodes.remove(email), 10, TimeUnit.MINUTES);

        emailService.sendEmail(emailDto);
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

    private String generateTempPassword() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

}
