package com.toy2.shop29.users.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class UserDto {
    private String userId; //로그인 ID
    private String password;  // 비밀번호
    private String userName;  // 회원 이름
    private String userRole; //회원 권한
    private String postalCode;  // 우편번호
    private String addressLine1;  // 상세 주소 1
    private String addressLine2;  // 상세 주소 2
    private String addressReference;  // 주소 참고 항목
    private String email;   // 이메일
    private String phoneNumber;  // 전화번호
    private String birthDate;  // 생년월일
    private Integer gender;  // 성별 (1: 남성, 0: 여성)
    private LocalDate registrationDate;  // 가입일자
    private Integer loginFailureCount;  // 로그인 실패 횟수
    private LocalDateTime lockExpiryTime;  // 계정 잠금 만료시간
    private LocalDateTime lastLoginFailureTime;  // 마지막 로그인 실패 시간
    private String joinType; // 제공자 : 네이버, 카카오 등
    private String providerId;  // 소셜로그인에서 제공하는 id
    private String userImage; // 회원 이미지
}
