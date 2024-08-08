package com.toy2.shop29.users.domain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@ToString
public class UserUpdateDto {

    private String userId;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).{8,20}$", message = "비밀번호는 8~20자, 대소문자 및 숫자로 이루어져야 합니다.")
    private String password;  // 비밀번호

    @NotBlank(message = "비밀번호 확인을 입력해주세요.")
    private String passwordConfirm; // 비밀번호 확인

    @NotBlank(message = "이름을 입력해주세요.")
    @Pattern(regexp = "^[^0-9]{2,15}$", message = "이름은 숫자를 포함할 수 없으며 2~15자 이내여야 합니다.")
    private String userName;  // 회원 이름

    @NotBlank(message = "전화번호를 입력해주세요.")
    @Pattern(regexp = "^\\d{3}-\\d{4}-\\d{4}$", message = "전화번호 형식이 올바르지 않습니다. ex) 010-1234-5678")
    private String phoneNumber;  // 전화번호

    @NotNull(message = "생년월일을 입력해주세요.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Pattern(regexp = "^(19[0-9][0-9]|20[0-1][0-9])-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$",
            message = "생년월일 형식이 올바르지 않습니다. ex) 1990-03-27")
    private String birthDate;  // 생년월일
    private int gender;  // 성별 (1: 남성, 0: 여성)
    private String postalCode;  // 우편번호
    private String addressLine1;  // 상세 주소 1
    private String addressLine2;  // 상세 주소 2
    private String addressReference;  // 주소 참고 항목

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;   // 이메일
}
