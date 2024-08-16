package com.toy2.shop29.users.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.toy2.shop29.users.domain.UserDto;
import com.toy2.shop29.users.domain.UserRegisterDto;
import com.toy2.shop29.users.exception.loginException.IncorrectPasswordException;
import com.toy2.shop29.users.exception.loginException.UserAccountLockedException;
import com.toy2.shop29.users.exception.loginException.UserNotFoundException;
import com.toy2.shop29.users.service.login.LoginService;
import com.toy2.shop29.users.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@SpringBootTest
class LoginServiceTest {
    @Autowired
    private UserService userService;
    @Autowired
    private LoginService loginService;
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

    @DisplayName("정상 로그인 테스트")
    @Test
    void validLogin() {
        // given: 유효한 아이디, 패스워드로 로그인 시도
        String validUserId = "testUser";
        String validPassword = "password123";

        // when: 로그인 시도
        UserDto loginUser = loginService.loginCheck(validUserId, validPassword);

        // then: 로그인 성공시 실패관련 컬럼 초기화 확인
        assertThat(loginUser.getLoginFailureCount()).isEqualTo(0);
        assertThat(loginUser.getLockExpiryTime()).isNull();
        assertThat(loginUser.getLastLoginFailureTime()).isNull();

        // 회원 확인
        UserDto user = userService.findById(loginUser.getUserId());
        assertThat(loginUser).isEqualTo(user);
    }


    @DisplayName("아이디를 잘못 입력한 경우 테스트")
    @Test
    void invalidId2() {
        // given: 존재하지 않는 아이디, 잘못된 패스워드로 로그인 시도
        String invalidUserId = "wrongUserId";
        String password = "password123";

        // then : UserNotFoundException이 발생하는지 확인
        assertThatThrownBy(() -> loginService.loginCheck(invalidUserId, password)).isInstanceOf(
                UserNotFoundException.class);
    }

    @DisplayName("패스워드를 잘못 입력한 경우 테스트")
    @Test
    void invalidPassword() {
        // given: 올바른 아이디, 잘못된 패스워드로 로그인 시도
        String validUserId = "testUser";
        String invalidPassword = "wrongPassword";

        // then : IncorrectPasswordException 발생하는지 확인
        assertThatThrownBy(() -> loginService.loginCheck(validUserId, invalidPassword)).isInstanceOf(
                IncorrectPasswordException.class);
    }

    @DisplayName("계정이 잠겨있는지 확인 테스트")
    @Test
    void isAccountLocked() {
        // given: 올바른 아이디, 잘못된 패스워드로 로그인 시도
        String validUserId = "testUser";
        String invalidPassword = "wrongPassword";

        // when: 비밀번호를 5번 틀리게 입력
        for (int i = 0; i < 5; i++) {
            assertThatThrownBy(() -> loginService.loginCheck(validUserId, invalidPassword)).isInstanceOf(
                    IncorrectPasswordException.class);
        }

        // then: 5번 실패 이후에는 계정이 잠겨야 함
        UserDto user = userService.findById(validUserId);
        assertThat(user.getLoginFailureCount()).isEqualTo(5);

        // UserAccountLockedException 발생하는지 확인
        assertThatThrownBy(() -> loginService.loginCheck(validUserId, invalidPassword))
                .isInstanceOf(UserAccountLockedException.class);
    }

//    @DisplayName("계정 잠금 해제 테스트")
//    @Test
//    void accountUnlocksAfterExpiryTime() throws AccountLockedException {
//        // given: 잠금 만료 시간이 지난 계정
//        String validUserId = "testUser";
//        String validPassword = "password123";
//
//        // 계정이 잠긴 상태로 설정
//        UserDto user = userService.findById(validUserId);
//        user.setLoginFailureCount(5);  // 로그인 실패 횟수 5회
//        user.setLockExpiryTime(LocalDateTime.now().minusMinutes(1));  // 잠금 만료 시간이 지난 상태
////        userService.updateUser(user.getUserId(), user);  // 갱신된 정보를 저장(문제 발생...)
//
//        // when: 계정 잠금 만료 후 올바른 비밀번호로 로그인 시도
//        UserDto loginUser = loginService.loginCheck(validUserId, validPassword);
//
//        // then: 로그인 성공 시 실패 관련 컬럼 초기화 확인
//        assertThat(loginUser.getLoginFailureCount()).isEqualTo(0);
//        assertThat(loginUser.getLockExpiryTime()).isNull();
//        assertThat(loginUser.getLastLoginFailureTime()).isNull();
//
//        // 회원 정보가 올바르게 반환되는지 확인
//        UserDto retrievedUser = userService.findById(loginUser.getUserId());
//        assertThat(loginUser).isEqualTo(retrievedUser);
//    }
}