package com.toy2.shop29.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

@Component
public class FormAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    // RequestCache는 인증되지 않은 사용자가 접근하려던 원래 요청을 저장하는 역할
    private final RequestCache requestCache = new HttpSessionRequestCache();

    // RedirectStrategy는 사용자를 다른 URL로 리다이렉트하는 전략을 정의합니다.
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        // 사용자가 접근하려던 url이 저장된 객체
        String redirectUrl = determineRedirectUrl(request, response);
        redirectStrategy.sendRedirect(request, response, redirectUrl);
    }

    private String determineRedirectUrl(HttpServletRequest request, HttpServletResponse response) {

        // 인증이 필요한 페이지 접근시, 사용자가 원래 요청했던 URL을 얻어옴
        SavedRequest savedRequest = requestCache.getRequest(request, response);

        if (savedRequest != null) {
            return savedRequest.getRedirectUrl();
        }

        // savedRequest가 없고 Referer 헤더가 있으면 Referer URL로 리다이렉트
        String sessionRedirectUrl = (String) request.getSession().getAttribute("redirectUrl");
        if (isInvalidRedirectUrl(sessionRedirectUrl)) {
            return "/";
        }
        // 세션에서 URL 제거 후 반환

        request.getSession().removeAttribute("redirectUrl");  // URL을 사용 후 세션에서 제거
        return sessionRedirectUrl;
    }

    private boolean isInvalidRedirectUrl(String redirectUrl) {
        return redirectUrl == null || redirectUrl.contains("/login");
    }
}
