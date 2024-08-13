package com.toy2.shop29.users.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.toy2.shop29.users.domain.UserDto;
import com.toy2.shop29.users.domain.UserRegisterDto;
import com.toy2.shop29.users.domain.UserUpdateDto;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

/**
 * 기본 회원 CRUD 테스트 회원 조회 회원 등록 회원 삭제 회원 수정
 */
@Transactional
@SpringBootTest
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;
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

        // 테스트 유저 생성
        userMapper.insertUser(userRegisterDto);
    }

    @DisplayName("회원id로 회원 조회 쿼리문 테스트")
    @Test
    void test1() {
        // 회원id로 찾기
        UserDto user = userMapper.findById(userRegisterDto.getUserId());
        // 회원 조회
        assertThat(user.getUserId()).isEqualTo(userRegisterDto.getUserId());
    }

    @DisplayName("이메일로 회원 조회 쿼리문 테스트")
    @Test
    void findByEmailTest() {

        String userId = userMapper.findByEmail(userRegisterDto.getEmail());

        assertThat(userId).isEqualTo(userRegisterDto.getUserId());
    }

    @DisplayName("전화번호로 회원 조회 쿼리문 테스트")
    @Test
    void findByPhoneNumberTest() {
        String userId = userMapper.findByPhoneNumber(userRegisterDto.getPhoneNumber());

        assertThat(userId).isEqualTo(userRegisterDto.getUserId());
    }

    @DisplayName("회원 가입 쿼리문 테스트")
    @Test
    void signup_test() {

        //테스트 유저 삭제
        userMapper.deleteUser(userRegisterDto.getUserId());

        int beforeInsertUserCnt = userMapper.userCount();

        //테스트 유저 회원가입
        int rowCnt = userMapper.insertUser(userRegisterDto);
        assertThat(rowCnt).isEqualTo(1);

        //가입된 회원
        UserDto registeredUser = userMapper.findById(userRegisterDto.getUserId());
        assertThat(registeredUser.getUserName()).isEqualTo(userRegisterDto.getUserName());

        int afterInsertUserCnt = userMapper.userCount();

        //가입전과 가입후 회원수 비교
        assertThat(beforeInsertUserCnt + 1).isEqualTo(afterInsertUserCnt);
    }


    @DisplayName("회원 삭제 쿼리문 테스트")
    @Test
    void test3() {
        int userCnt1 = userMapper.userCount();

        int rowCnt = userMapper.deleteUser(userRegisterDto.getUserId());
        assertThat(rowCnt).isEqualTo(1);

        UserDto deleteUser = userMapper.findById(userRegisterDto.getUserId());
        assertThat(deleteUser).isNull();

        int userCnt2 = userMapper.userCount();

        //삭제전과 삭제후의 회원 수 비교
        assertThat(userCnt1 - 1).isEqualTo(userCnt2);

    }

    @DisplayName("회원 수정 쿼리문 테스트")
    @Test
    void testUpdateUser() {
        System.out.println("userRegisterDto = " + userRegisterDto);

        UserUpdateDto userUpdateDto = new UserUpdateDto();

        userUpdateDto.setPostalCode("12345");
        userUpdateDto.setAddressLine1("경기 화성시");
        userUpdateDto.setAddressLine2("201호");
        userUpdateDto.setAddressReference("");
        userUpdateDto.setGender(1);

        //업데이트 부분
        userUpdateDto.setPassword("UpdatedPassword123");
        userUpdateDto.setUserName("Updated Name");
        userUpdateDto.setPhoneNumber("010-9876-5432");
        userUpdateDto.setEmail("updated@example.com");
        userUpdateDto.setBirthDate("1990-04-14");

        // 업데이트 실행
        int updateCount = userMapper.updateUser("testUser", userUpdateDto);
        assertThat(updateCount).isEqualTo(1);

        // 업데이트 후 데이터 확인
        UserDto updatedUser = userMapper.findById("testUser");
        System.out.println("updatedUser = " + updatedUser);

        System.out.println("updatedUser.getBirthDate() = " + updatedUser.getBirthDate());
        assertThat(updatedUser.getUserName()).isEqualTo("Updated Name");
        assertThat(updatedUser.getPassword()).isEqualTo("UpdatedPassword123");
        assertThat(updatedUser.getPhoneNumber()).isEqualTo("010-9876-5432");
        assertThat(updatedUser.getEmail()).isEqualTo("updated@example.com");
        assertThat(updatedUser.getBirthDate()).isEqualTo("1990-04-14");

        // 수정되지 않은 필드 확인
        assertThat(updatedUser.getGender()).isEqualTo(userRegisterDto.getGender());
        assertThat(updatedUser.getPostalCode()).isEqualTo(userRegisterDto.getPostalCode());
        assertThat(updatedUser.getAddressLine1()).isEqualTo(userRegisterDto.getAddressLine1());
        assertThat(updatedUser.getAddressLine2()).isEqualTo(userRegisterDto.getAddressLine2());
        assertThat(updatedUser.getAddressReference()).isEqualTo(userRegisterDto.getAddressReference());

    }

    @DisplayName("로그인 실패 횟수 증가 쿼리문 테스트")
    @Test
    void shouldIncreaseLoginFailureCount() {
        // Given: 기존 유저의 로그인 실패 횟수를 조회
        UserDto initialUser = userMapper.findById(userRegisterDto.getUserId());
        int initialFailureCount = initialUser.getLoginFailureCount();
        LocalDateTime initialFailureTime = initialUser.getLastLoginFailureTime();

        // When: 로그인 실패 횟수를 증가시키는 쿼리 실행
        int affectedRows = userMapper.increaseLoginFailureCount(userRegisterDto.getUserId());
        assertThat(affectedRows).isEqualTo(1);  // 1개의 행이 영향을 받아야 함

        // Then: 업데이트된 유저의 로그인 실패 횟수 다시 조회
        UserDto updatedUser = userMapper.findById(userRegisterDto.getUserId());
        int updatedFailureCount = updatedUser.getLoginFailureCount();
        LocalDateTime updatedFailureTime = updatedUser.getLastLoginFailureTime();

        System.out.println("initialUser = " + initialUser);
        System.out.println("updatedUser = " + updatedUser);

        // 로그인 실패 횟수가 1 증가했는지 검증
        assertThat(updatedFailureCount).isEqualTo(initialFailureCount + 1);

        // 최근 로그인 실패시간 검증
        assertThat(initialFailureTime).isNotEqualTo(updatedFailureTime);
    }

    @DisplayName("계정 잠금 시간 설정 쿼리문 테스트")
    @Test
    void lockAccountTest() {
        // When: 계정 잠금 메소드 호출
        int rowCnt = userMapper.lockAccount(userRegisterDto.getUserId());
        assertThat(rowCnt).isEqualTo(1);

        // Then: 계정이 잠금 상태가 되었는지 확인
        UserDto updatedUser = userMapper.findById(userRegisterDto.getUserId());
        assertThat(updatedUser.getLockExpiryTime()).isNotNull();
        assertThat(updatedUser.getLockExpiryTime()).isAfter(LocalDateTime.now());
    }

    @DisplayName("로그인 성공 시 초기화 쿼리문 테스트")
    @Test
    void resetLoginFailureCountTest() {
        // Given: 먼저 실패 횟수를 증가시킴
        int rowCnt = userMapper.increaseLoginFailureCount(userRegisterDto.getUserId());
        assertThat(rowCnt).isEqualTo(1);

        // When: 로그인 성공 시 초기화 메소드 호출
        rowCnt = userMapper.resetLoginFailureCount(userRegisterDto.getUserId());
        assertThat(rowCnt).isEqualTo(1);

        // Then: 로그인 실패 횟수와 잠금 상태가 초기화되었는지 확인
        UserDto updatedUser = userMapper.findById(userRegisterDto.getUserId());
        assertThat(updatedUser.getLoginFailureCount()).isEqualTo(0);
        assertThat(updatedUser.getLockExpiryTime()).isNull();
        assertThat(updatedUser.getLastLoginFailureTime()).isNull();

    }

}