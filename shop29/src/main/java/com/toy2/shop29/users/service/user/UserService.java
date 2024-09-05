package com.toy2.shop29.users.service.user;

import com.toy2.shop29.users.domain.UserDto;
import com.toy2.shop29.users.domain.UserRegisterDto;
import com.toy2.shop29.users.domain.UserUpdateDto;
import com.toy2.shop29.users.domain.UserWithdrawalDto;
import org.apache.ibatis.annotations.Param;
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

    int updatePassword(String userId, String tempPassword);

    boolean isUserIdDuplicated(String userId);

    boolean isEmailDuplicated(String email);

    boolean isPhoneNumberDuplicated(String phoneNumber);

    // 회원 탈퇴시 탈퇴테이블에 회원 추가
    int insertWithdrawalUser(String userId, UserWithdrawalDto withdrawalDto);
}
