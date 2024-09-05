package com.toy2.shop29.users.service.email;

import com.toy2.shop29.users.domain.EmailDto;
import com.toy2.shop29.users.service.user.UserService;
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
    private final EmailService emailService;
    private final UserService userService;
    private final ScheduledExecutorService schedular = Executors.newScheduledThreadPool(1);

    // 인증 코드 생성
    public void generateVerificationCode(String email) {
        String verificationCode = generateVerificationCode();
        // EmailDto 생성
        EmailDto emailDto = EmailDto.builder()
                .subject("이메일 인증을 위한 인증번호를 안내 드립니다.")
                .body("다음 인증 코드를 사용해 주세요 : " + "\n인증 코드 : " + verificationCode)
                .emailRecipent(email)
                .code(verificationCode)
                .build();

        saveEmailInfo(email, verificationCode);
        emailService.sendEmail(emailDto);
    }

    // 임시 비밀번호 생성
    public void generateTempPassword(String userId, String email) {
        String tempPassword = generateTempPassword();
        // EmailDto 생성
        EmailDto emailDto = EmailDto.builder()
                .subject("임시 비밀번호입니다.")
                .body("다음 임시 비밀번호를 사용해 주세요 : " + "\n임시 비밀번호 : " + tempPassword)
                .emailRecipent(email)
                .code(tempPassword)
                .build();
        saveEmailInfo(email, tempPassword);
        emailService.sendEmail(emailDto);
        //임시 비밀번호 저장
        userService.updatePassword(userId, tempPassword);
    }

    public boolean verifyCode(String email, String code) {
        String storedCode = verificationCodes.get(email);
        System.out.println("저장코드" + storedCode);
        System.out.println(verificationCodes);
        return storedCode != null && storedCode.equals(code);
    }

    private void saveEmailInfo(String email, String code) {
        verificationCodes.put(email, code);
        // 10분 후에 자동으로 인증코드 삭제
        schedular.schedule(() -> verificationCodes.remove(email), 10, TimeUnit.MINUTES);
    }

    private String generateVerificationCode() {
        return UUID.randomUUID().toString().substring(0, 6);
    }

    private String generateTempPassword() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

}
