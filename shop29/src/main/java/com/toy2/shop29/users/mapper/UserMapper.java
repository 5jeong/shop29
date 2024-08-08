package com.toy2.shop29.users.mapper;

import com.toy2.shop29.users.domain.UserDto;
import com.toy2.shop29.users.domain.UserRegisterDto;
import com.toy2.shop29.users.domain.UserUpdateDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    UserDto findById(String userId); // id로 회원 조회
    UserDto findByEmail(String userId); // email로 회원 조회
    UserDto findByPhoneNumber(String userId); // id로 회원 조회
    int insertUser(UserRegisterDto userRegisterDto); // 회원가입
    int deleteUser(String userId); // id로 회원 삭제
    int updateUser(@Param("userId") String userId, @Param("userUpdateDto") UserUpdateDto userUpdateDto); //회원 수정
    int userCount(); // 회원 갯수 세기
    boolean exisetedUserId();
}

