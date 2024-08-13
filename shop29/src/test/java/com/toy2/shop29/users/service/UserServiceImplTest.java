package com.toy2.shop29.users.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.toy2.shop29.users.domain.UserRegisterDto;
import java.time.LocalDate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Transactional
@SpringBootTest
class UserServiceImplTest {
    @Autowired
    private UserServiceImpl userService;
    private UserRegisterDto userRegisterDto;

    @BeforeEach
    void setUp() {
        // given: 회원 등록 셋팅
        userRegisterDto = new UserRegisterDto();
        userRegisterDto.setUserId("testUser");
        userRegisterDto.setEmail("testuser@example.com");
        userRegisterDto.setPassword("password123");
        userRegisterDto.setUserName("손흥민");
        userRegisterDto.setPostalCode("12345");
        userRegisterDto.setAddressLine1("경기 화성시");
        userRegisterDto.setAddressLine2("201호");
        userRegisterDto.setAddressReference("");
        userRegisterDto.setPhoneNumber("010-1234-9999");
        userRegisterDto.setGender(1); // 1은 남자
        userRegisterDto.setBirthDate("1990-02-12");
        userService.insertUser(userRegisterDto);
    }

    @DisplayName("중복된 정보가 없을경우 테스트")
    @Test
    void validateDuplicatedInfoTest() {
        // Given: 새로운 UserRegisterDto 설정 (중복되는 데이터 사용)
        UserRegisterDto newUserDto = new UserRegisterDto();
        newUserDto.setUserId("uniqueUser"); // 중복되지않은 아이디
        newUserDto.setEmail("unique@example.com"); // 중복되지않은 이메일
        newUserDto.setPhoneNumber("010-1111-2222"); // 중복되지않은 전화번호

        // BindingResult 객체 생성
        BindingResult bindingResult = new BeanPropertyBindingResult(newUserDto, "userRegisterDto");

        // When: validateDuplicatedInfo 호출
        userService.validateDuplicatedInfo(newUserDto, bindingResult);

        // Then: 중복 에러가 발생하지 않는지 확인
        assertThat(bindingResult.hasFieldErrors("userId")).isFalse();
        assertThat(bindingResult.hasFieldErrors("email")).isFalse();
        assertThat(bindingResult.hasFieldErrors("phoneNumber")).isFalse();
    }



    @DisplayName("중복 회원 테스트")
    @Test
    void idDuplicateTest (){
        // Given: 새로운 UserRegisterDto 설정 (중복되는 데이터 사용)
        UserRegisterDto newUserDto = new UserRegisterDto();
        newUserDto.setUserId("testUser"); // 중복 아이디

        // BindingResult 객체 생성
        BindingResult bindingResult = new BeanPropertyBindingResult(newUserDto, "userRegisterDto");

        // When: validateDuplicatedInfo 호출
        userService.validateDuplicatedInfo(newUserDto, bindingResult);

        // Then: 중복 에러가 발생했는지 확인
        assertThat(bindingResult.hasFieldErrors("userId")).isTrue();

    }

    @DisplayName("중복 이메일 테스트")
    @Test
    void emailDuplicateTest (){
        // Given: 새로운 UserRegisterDto 설정 (중복되는 데이터 사용)
        UserRegisterDto newUserDto = new UserRegisterDto();
        newUserDto.setEmail("testuser@example.com"); // 중복 이메일

        // BindingResult 객체 생성
        BindingResult bindingResult = new BeanPropertyBindingResult(newUserDto, "userRegisterDto");

        // When: validateDuplicatedInfo 호출
        userService.validateDuplicatedInfo(newUserDto, bindingResult);

        // Then: 중복 에러가 발생했는지 확인
        assertThat(bindingResult.hasFieldErrors("email")).isTrue();
     }

    @DisplayName("중복 휴대폰번호 테스트")
    @Test
    void phoneDuplicateTest (){
        // Given: 새로운 UserRegisterDto 설정 (중복되는 데이터 사용)
        UserRegisterDto newUserDto = new UserRegisterDto();
        newUserDto.setPhoneNumber("010-1234-9999"); // 중복 전화번호

        // BindingResult 객체 생성
        BindingResult bindingResult = new BeanPropertyBindingResult(newUserDto, "userRegisterDto");

        // When: validateDuplicatedInfo 호출
        userService.validateDuplicatedInfo(newUserDto, bindingResult);
        // Then: 중복 에러가 발생했는지 확인
        assertThat(bindingResult.hasFieldErrors("phoneNumber")).isTrue();
    }
}