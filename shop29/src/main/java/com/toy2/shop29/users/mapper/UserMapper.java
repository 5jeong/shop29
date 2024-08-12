package com.toy2.shop29.users.mapper;

import com.toy2.shop29.users.domain.UserDto;
import com.toy2.shop29.users.domain.UserRegisterDto;
import com.toy2.shop29.users.domain.UserUpdateDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    // id로 회원 조회
    UserDto findById(String userId);

    // email로 회원 조회
    String findByEmail(String email);

    // phoneNumber로 회원 조회
    String findByPhoneNumber(String phoneNumber);

    // 회원가입
    int insertUser(UserRegisterDto userRegisterDto);

    // id로 회원 삭제
    int deleteUser(String userId);

    //회원 수정
    int updateUser(@Param("userId") String userId, @Param("userUpdateDto") UserUpdateDto userUpdateDto);

    // 회원 갯수 세기
    int userCount();

    // 로그인 실패 횟수 증가
    int increaseLoginFailureCount(String userId);

    // 계정 잠금 시간 설정
    int lockAccount(String userId);

    // 로그인 성공 또는 계정 잠금이 풀린경우 잠금관련 컬럼 초기화
    int resetLoginFailureCount(String userId);

}

