package com.toy2.shop29.security.handler;

import com.toy2.shop29.users.domain.UserDto;
import com.toy2.shop29.users.exception.loginException.EmptyFieldException;
import com.toy2.shop29.users.exception.loginException.UserAccountLockedException;
import com.toy2.shop29.users.mapper.UserMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FormAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final UserMapper userMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        String userId = request.getParameter("userId");
        UserDto user = userMapper.findById(userId);

        // 로그인 실패 처리
        if (user != null) {
            handlerLoginFailure(user);
        }

        // 예외 유형에 따라 에러 메시지 생성
        String errorMessage = determineErrorMessage(exception, user);

        // 에러 메시지를 인코딩하여 URL에 포함
        String encodedErrorMessage = URLEncoder.encode(errorMessage, StandardCharsets.UTF_8);
        setDefaultFailureUrl("/login?error=true&exception=" + encodedErrorMessage);

        // 기본 처리 호출
        super.onAuthenticationFailure(request, response, exception);
    }

    private void handlerLoginFailure(UserDto user) {
        userMapper.increaseLoginFailureCount(user.getUserId());

        if (user.getLoginFailureCount() + 1 >= 5) {
            userMapper.lockAccount(user.getUserId());
        }
    }

    private String determineErrorMessage(AuthenticationException exception, UserDto user) {
        if (exception instanceof EmptyFieldException) {
            return exception.getMessage();
        } else if (exception instanceof UsernameNotFoundException) {
            return exception.getMessage();
        } else if (exception instanceof BadCredentialsException) {
            return "비밀번호가 일치하지 않습니다. '5회 로그인 실패 시 로그인이 10분 동안 제한됩니다."
                    + " (" + (user.getLoginFailureCount() + 1) + "/5)";
        } else if (exception instanceof UserAccountLockedException) {
            return exception.getMessage();
        } else {
            return "알 수 없는 오류가 발생했습니다.";
        }
    }
}

