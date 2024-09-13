package com.toy2.shop29.security.provider;

import com.toy2.shop29.timeProvider.TimeProvider;
import com.toy2.shop29.users.domain.UserContext;
import com.toy2.shop29.users.domain.UserDto;
import com.toy2.shop29.users.exception.loginException.EmptyFieldException;
import com.toy2.shop29.users.exception.loginException.UserAccountLockedException;
import com.toy2.shop29.users.mapper.UserMapper;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FormAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final TimeProvider timeProvider; // 의존성 주입으로 시의간 공급자를 받음


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userId = authentication.getName();
        UserDto user = userMapper.findById(userId);
        // 사용자가 입력한 비밀번호 가져오기
        String password = (String) authentication.getCredentials();

        // 공백 확인: 아이디 또는 비밀번호가 비어있으면 커스텀 예외 던짐
        if (userId == null || userId.trim().isEmpty()) {
            throw new EmptyFieldException("아이디를 입력해주세요.");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new EmptyFieldException("비밀번호를 입력해주세요.");
        }
        if (user == null) {
            throw new UsernameNotFoundException(userId + "로 가입된 아이디가 없습니다.");
        }

        // 계정 잠금 확인 및 처리
        checkAccountLock(user);

        UserContext userContext = (UserContext) userDetailsService.loadUserByUsername(userId);

        // 비밀번호 확인( 실제 서비스에는 이렇게 해야됨 )
//        if (!passwordEncoder.matches(password, userContext.getPassword())) {
//            throw new BadCredentialsException("비밀번호를 잘못 입력");
//        }

        // SQL insert문에 암호화 안된 비밀번호도 작동하게끔 설정
        if (!password.equals(user.getPassword()) && !passwordEncoder.matches(password, userContext.getPassword())) {
            throw new BadCredentialsException("비밀번호를 잘못 입력");
        }

        // 로그인 성공시 로그인 실패 관련 컬럼 초기화
        userMapper.resetLoginFailureCount(userId);

        // principal 타입으로 UserDto을 받을 수 있게 토큰 설정
        return new UsernamePasswordAuthenticationToken(userContext, null, userContext.getAuthorities());

    }

    //UsernamePasswordAuthenticationToken 타입의 인증을 지원
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
    }

    // 계정 잠금 확인
    private void checkAccountLock(UserDto user) {

        // 계정 잠금 해제여부 확인
        if (shouldUnlockAccount(user)) {
            resetFailureInfo(user.getUserId());
        }

        // 계정이 잠겨있는 경우
        if (isAccountLocked(user)) {
            String lockExpiryTime = user.getLockExpiryTime()
                    .format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초"));
            throw new UserAccountLockedException("계정이 잠겨 있습니다. " + lockExpiryTime + " 이후에 다시 시도하세요.");
        }

    }

    // 계정 잠금 해제 여부를 확인하는 메서드
    private boolean shouldUnlockAccount(UserDto user) {
        LocalDateTime lockExpiryTime = user.getLockExpiryTime();
        return lockExpiryTime != null && lockExpiryTime.isBefore(timeProvider.now());
    }

    private void resetFailureInfo(String userId) {
        userMapper.resetLoginFailureCount(userId);
    }

    private boolean isAccountLocked(UserDto user) {
        LocalDateTime lockExpiryTime = user.getLockExpiryTime();
        return lockExpiryTime != null && lockExpiryTime.isAfter(timeProvider.now());
    }
}