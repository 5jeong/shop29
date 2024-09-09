package com.toy2.shop29.users.mapper;

import com.toy2.shop29.users.domain.UserDto;
import com.toy2.shop29.users.domain.UserRegisterDto;
import com.toy2.shop29.users.domain.UserUpdateDto;
import com.toy2.shop29.users.domain.UserWithdrawalDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface UserMapper {
    // id로 회원 조회
    UserDto findById(String userId);

    // email로 회원id 조회
    String findByEmail(String email);

    // phoneNumber로 회원 조회
    String findByPhoneNumber(String phoneNumber);

    // 회원가입
    int insertUser(UserRegisterDto userRegisterDto);

    // id로 회원 삭제
    int deleteUser(String userId);

    // 회원탈퇴 테이블에 회원추가
    int insertWithdrawalUser(@Param("userId") String userId, @Param("withdrawalDto") UserWithdrawalDto withdrawalDto);

    //회원수정 폼에서 수정
    int updateUser(@Param("userId") String userId, @Param("userUpdateDto") UserUpdateDto userUpdateDto);

    // 회원 갯수 세기
    int userCount();

    // 로그인 실패 횟수 증가
    int increaseLoginFailureCount(String userId);

    // 계정 잠금 시간 설정
    int lockAccount(String userId);

    // 로그인 성공 또는 계정 잠금이 풀린경우 잠금관련 컬럼 초기화
    int resetLoginFailureCount(String userId);

    // 비밀번호 변경
    int updatePassword(@Param("userId") String userId, @Param("tempPassword") String tempPassword);

    // 테스트용 메서드 - 작성자 : 김정민(24.08.16)
    int updateUserRoleForTest(Map<String,Object> map);


    // 소셜 로그인 사용자 저장
    int insertSocialUser(UserDto userDto);

    // provider와 providerId로 소셜 로그인 사용자 조회
    UserDto findByProviderId( String providerId);
}

