package com.toy2.shop29.users.validation;

import static org.assertj.core.api.Assertions.assertThat;

import com.toy2.shop29.users.domain.UserRegisterDto;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

public class UserRegisterDtoValidationTest {

    // 필드 정의
    private final LocalValidatorFactoryBean validator;

    public UserRegisterDtoValidationTest() {
        this.validator = new LocalValidatorFactoryBean();
        this.validator.afterPropertiesSet(); // 초기화
    }

    @DisplayName("유효한 회원가입 DTO 테스트")
    @Test
    void validUserRegisterDtoTest() {
        // Given : 유효한 회원가입정보
        UserRegisterDto userRegisterDto = new UserRegisterDto();
        userRegisterDto.setUserId("validUser");
        userRegisterDto.setPassword("validPass123");
        userRegisterDto.setUserName("홍길동");
        userRegisterDto.setPhoneNumber("010-1234-5678");
        userRegisterDto.setBirthDate(("1990-03-27"));
        userRegisterDto.setGender(1);
        userRegisterDto.setPostalCode("12345");
        userRegisterDto.setAddressLine1("서울특별시");
        userRegisterDto.setAddressLine2("강남구");
        userRegisterDto.setAddressReference("");
        userRegisterDto.setEmail("validuser@example.com");

        // When
        BindingResult bindingResult = new BeanPropertyBindingResult(userRegisterDto, "userRegisterDto");
        validator.validate(userRegisterDto, bindingResult);

        // Then
        assertThat(bindingResult.hasErrors()).isFalse();
    }

    // 아이디는 4~15자 이내여야 합니다
    @DisplayName("유효하지 않은 아이디 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"", "a", "aaa", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaa"})
    void invalidUserIdTest(String userId) {
        // Given
        UserRegisterDto userRegisterDto = new UserRegisterDto();
        userRegisterDto.setUserId(userId);  // Invalid: Too short

        // When
        BindingResult bindingResult = new BeanPropertyBindingResult(userRegisterDto, "userRegisterDto");
        validator.validate(userRegisterDto, bindingResult);

        // Then
        assertThat(bindingResult.hasFieldErrors("userId")).isTrue();
    }

    // 비밀번호는 8~20자, 대소문자 및 숫자로 이루어져야 합니다.
    @DisplayName("유효하지 않은 비밀번호 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"", "aaa", "aaaaaaaaaa", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"})
    void invalidPasswordTest(String password) {
        // Given
        UserRegisterDto userRegisterDto = new UserRegisterDto();
        userRegisterDto.setPassword(password);

        // When
        BindingResult bindingResult = new BeanPropertyBindingResult(userRegisterDto, "userRegisterDto");
        validator.validate(userRegisterDto, bindingResult);

        // Then
        assertThat(bindingResult.hasFieldErrors("password")).isTrue();
    }

    @DisplayName("유효하지 않은 이름 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"", "1", "a", "홍", "12345홍길동", "이름이너무길어서유효하지않은경우"})
    void invalidUserNameTest(String userName) {
        // Given
        UserRegisterDto userRegisterDto = new UserRegisterDto();
        userRegisterDto.setUserName(userName);

        // When
        BindingResult bindingResult = new BeanPropertyBindingResult(userRegisterDto, "userRegisterDto");
        validator.validate(userRegisterDto, bindingResult);

        // Then
        assertThat(bindingResult.hasFieldErrors("userName")).isTrue();
    }

    @DisplayName("유효하지 않은 전화번호 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"", "01012345678", "010-1234-567", "010-12345-6789", "010-1234-567a"})
    void invalidPhoneNumberTest(String phoneNumber) {
        // Given
        UserRegisterDto userRegisterDto = new UserRegisterDto();
        userRegisterDto.setPhoneNumber(phoneNumber);

        // When
        BindingResult bindingResult = new BeanPropertyBindingResult(userRegisterDto, "userRegisterDto");
        validator.validate(userRegisterDto, bindingResult);

        // Then
        assertThat(bindingResult.hasFieldErrors("phoneNumber")).isTrue();
    }


    @DisplayName("유효하지 않은 생년월일 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"", "1990-00-00", "2021-13-01", "abcd-ef-gh", "1990/01/01", "19900229"})
    void invalidBirthDateTest(String birthDate) {
        // Given
        UserRegisterDto userRegisterDto = new UserRegisterDto();
        userRegisterDto.setBirthDate(birthDate);

        // When
        BindingResult bindingResult = new BeanPropertyBindingResult(userRegisterDto, "userRegisterDto");
        validator.validate(userRegisterDto, bindingResult);

        // Then
        assertThat(bindingResult.hasFieldErrors("birthDate")).isTrue();
    }

    @DisplayName("유효하지 않은 이메일 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"", "plainaddress.", "missingatsign.com", "user@.missingdomain", "user@domain...com"})
    void invalidEmailTest(String email) {
        // Given
        UserRegisterDto userRegisterDto = new UserRegisterDto();
        userRegisterDto.setEmail(email);

        // When
        BindingResult bindingResult = new BeanPropertyBindingResult(userRegisterDto, "userRegisterDto");
        validator.validate(userRegisterDto, bindingResult);

        // Then
        assertThat(bindingResult.hasFieldErrors("email")).isTrue();
    }

}
