package com.toy2.shop29.users.service;

import com.toy2.shop29.users.domain.UserDto;
import com.toy2.shop29.users.domain.UserRegisterDto;
import com.toy2.shop29.users.domain.UserUpdateDto;
import com.toy2.shop29.users.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;


    @Override
    public UserDto findById(String userId) {
        return userMapper.findById(userId);
    }

    @Override
    public String findByEmail(String email) {
        return userMapper.findByEmail(email);
    }

    @Override
    public String findByPhoneNumber(String phoneNumber) {
        return userMapper.findByPhoneNumber(phoneNumber);
    }

    @Override
    public int insertUser(UserRegisterDto userRegisterDto) {
        return userMapper.insertUser(userRegisterDto);
    }

    @Override
    public int updateUser(String userId, UserUpdateDto userUpdateDto) {
        userMapper.updateUser(userId, userUpdateDto);
        return 0;
    }

    @Override
    public int deleteUser(String userId) {
        return userMapper.deleteUser(userId);
    }

    @Override
    public void validateDuplicatedInfo(UserRegisterDto userRegisterDto, BindingResult bindingResult) {
        if (isUserIdDuplicated(userRegisterDto.getUserId())) {
            bindingResult.rejectValue("userId", "duplicateId");
        }
        if (isPhoneNumberDuplicated(userRegisterDto.getPhoneNumber())) {
            bindingResult.rejectValue("phoneNumber", "duplicatePhoneNumber");
        }
        if (isEmailDuplicated(userRegisterDto.getEmail())) {
            bindingResult.rejectValue("email", "duplicateEmail");
        }
    }

    public boolean isUserIdDuplicated(String userId) {
        return findById(userId) != null;
    }

    private boolean isEmailDuplicated(String email) {
        return findByEmail(email) != null;
    }

    private boolean isPhoneNumberDuplicated(String phoneNumber) {
        return findByPhoneNumber(phoneNumber) != null;
    }


}
