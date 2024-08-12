package com.toy2.shop29.users.service;

import com.toy2.shop29.users.domain.UserDto;
import com.toy2.shop29.users.domain.UserRegisterDto;
import com.toy2.shop29.users.domain.UserUpdateDto;
import org.springframework.validation.BindingResult;

public interface UserService {
    // id로 회원 조회
    UserDto findById(String userId);

    // 가입된 email로 회원 id조회
    String findByEmail(String email);

    // 가입된 전화번호로 회원 id조회
    String findByPhoneNumber(String phoneNumber);

    int insertUser(UserRegisterDto userRegisterDto);

    int updateUser(String userId, UserUpdateDto userUpdateDto);

    int deleteUser(String userId);

    void validateDuplicatedInfo(UserRegisterDto userRegisterDto, BindingResult bindingResult);
}
