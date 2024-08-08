package com.toy2.shop29.users.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserDto {
    private String userId;  // 로그인 ID
    private String email;   // 이메일
    private String password;  // 비밀번호
//    private String passwordConfirm;  // 비밀번호 확인
    private String userName;  // 회원 이름
    private String postalCode;  // 우편번호
    private String addressLine1;  // 상세 주소 1
    private String addressLine2;  // 상세 주소 2
    private String addressReference;  // 주소 참고 항목
    private String phoneNumber;  // 전화번호
    private LocalDate birthDate;  // 생년월일
    private int gender;  // 성별 (1: 남성, 0: 여성)
    private LocalDate registrationDate;  // 가입일자
    private LocalDate withdrawalDate;  // 탈퇴일자
    private int loginFailureCount;  // 로그인 실패 횟수
    private LocalDateTime lockExpiryTime;  // 계정 잠금 만료시간
    private LocalDateTime lastLoginFailureTime;  // 마지막 로그인 실패 시간
}
