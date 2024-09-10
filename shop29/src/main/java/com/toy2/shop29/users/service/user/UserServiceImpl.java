package com.toy2.shop29.users.service.user;

import com.toy2.shop29.users.domain.UserContext;
import com.toy2.shop29.users.domain.UserDto;
import com.toy2.shop29.users.domain.UserRegisterDto;
import com.toy2.shop29.users.domain.UserUpdateDto;
import com.toy2.shop29.users.domain.UserWithdrawalDto;
import com.toy2.shop29.users.mapper.UserMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

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
        userRegisterDto.setPassword(passwordEncoder.encode(userRegisterDto.getPassword()));
        return userMapper.insertUser(userRegisterDto);
    }

    @Override
    public int updateUser(String userId, UserUpdateDto userUpdateDto) {
        userUpdateDto.setPassword(passwordEncoder.encode(userUpdateDto.getPassword()));
        int result = userMapper.updateUser(userId, userUpdateDto);

        // 사용자 정보 수정 후 SecurityContext의 Authentication 객체 갱신
        updateAuthentication(userId);

        return result;
    }

    private void updateAuthentication(String userId) {
        UserDto updatedUser = userMapper.findById(userId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 새로 업데이트된 UserDto로 새로운 Authentication 객체 생성
        UserContext updatedUserContext = new UserContext(updatedUser,
                (List<GrantedAuthority>) authentication.getAuthorities());

        // SecurityContextHolder에 새로운 Authentication 객체 저장
        Authentication newAuth = new UsernamePasswordAuthenticationToken(updatedUserContext,
                authentication.getCredentials(), updatedUserContext.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }

    @Override
    public int deleteUser(String userId) {
        return userMapper.deleteUser(userId);
    }

    @Override
    public void validateDuplicatedInfo(UserRegisterDto userRegisterDto, BindingResult bindingResult) {
        if (isUserIdDuplicated(userRegisterDto.getUserId())) {
            bindingResult.rejectValue("userId", "duplicateId");
            throw new IllegalArgumentException("회원 id가 중복되었습니다.");
        }
        if (isPhoneNumberDuplicated(userRegisterDto.getPhoneNumber())) {
            bindingResult.rejectValue("phoneNumber", "duplicatePhoneNumber");
        }
        if (isEmailDuplicated(userRegisterDto.getEmail())) {
            bindingResult.rejectValue("email", "duplicateEmail");
        }
    }

    @Override
    public int updatePassword(String userId, String tempPassword) {
        return userMapper.updatePassword(userId, passwordEncoder.encode(tempPassword));
    }

    public boolean isUserIdDuplicated(String userId) {
        return findById(userId) != null;
    }

    public boolean isEmailDuplicated(String email) {
        return findByEmail(email) != null;
    }

    public boolean isPhoneNumberDuplicated(String phoneNumber) {
        return findByPhoneNumber(phoneNumber) != null;
    }

    @Override
    public int insertWithdrawalUser(String userId, UserWithdrawalDto withdrawalDto) {
        deleteUser(userId);
        return userMapper.insertWithdrawalUser(userId, withdrawalDto);
    }

    @Override
    public int saveSocialUser(UserDto userDto) {
        return userMapper.insertSocialUser(userDto);
    }

    @Override
    public int updateUserImage(String userId, String userImage) {
        updateAuthentication(userId);
        return userMapper.updateUserImage(userId,userImage);
    }

}