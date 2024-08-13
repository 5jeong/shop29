package com.toy2.shop29.users.service;

import com.toy2.shop29.users.domain.UserDto;
import com.toy2.shop29.users.exception.IncorrectPasswordException;
import com.toy2.shop29.users.exception.UserAccountLockedException;
import com.toy2.shop29.users.exception.UserNotFoundException;
import com.toy2.shop29.users.mapper.UserMapper;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserMapper userMapper;

    public UserDto loginCheck(String userId, String password) {
        UserDto user = userMapper.findById(userId);

        // 아이디를 잘못 입력한 경우
        if (user == null) {
            throw new UserNotFoundException(userId + "로 가입된 아이디가 없습니다.");
        }

        // 계정 잠금 해제여부 확인
        if (shouldUnlockAccount(user)) {
            resetFailureInfo(userId);
        }

        // 계정이 잠겨있는 경우
        if (isAccountLocked(user)) {
            String lockExpiryTime =
                    String.valueOf(user.getLockExpiryTime()).split("T")[0] + " " + String.valueOf(
                                    user.getLockExpiryTime())
                            .split("T")[1];
            throw new UserAccountLockedException("계정이 잠겨 있습니다. " + lockExpiryTime + " 이후에 다시 시도하세요.");
        }

        // 비밀번호를 잘못 입력한 경우
        if (invalidPassword(password, user)) {
            handlerLoginFailure(user);

            Integer loginFailureCount = user.getLoginFailureCount()+1;
            throw new IncorrectPasswordException(
                    "비밀번호가 일치하지않습니다. '5회 로그인 실패 시 로그인이 10분 동안 제한됩니다. (" + loginFailureCount + "/5)");
        }

        // 로그인 성공시 로그인 실패 관련 컬럼 초기화
        resetFailureInfo(userId);
        return user;
    }

    private void handlerLoginFailure(UserDto user) {
        userMapper.increaseLoginFailureCount(user.getUserId());
        if (user.getLoginFailureCount() + 1 >= 5) {
            userMapper.lockAccount(user.getUserId());
        }
    }

    // 계정 잠금 해제 여부를 확인하는 메서드
    private boolean shouldUnlockAccount(UserDto user) {
        LocalDateTime lockExpiryTime = user.getLockExpiryTime();
        return lockExpiryTime != null && lockExpiryTime.isBefore(LocalDateTime.now());
    }

    private void resetFailureInfo(String userId) {
        userMapper.resetLoginFailureCount(userId);
    }

    private static boolean isAccountLocked(UserDto user) {
        LocalDateTime lockExpiryTime = user.getLockExpiryTime();
        return lockExpiryTime != null && lockExpiryTime.isAfter(LocalDateTime.now());
    }

    private static boolean invalidPassword(String password, UserDto user) {
        return !user.getPassword().equals(password);
    }

}
