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
        String redirectUrl = "/";
        // 사용자가 접근하려던 url이 저장된 객체
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        System.out.println("원래 URL = " + savedRequest);
        if (savedRequest != null) {
            redirectUrl = savedRequest.getRedirectUrl();
            redirectStrategy.sendRedirect(request, response, redirectUrl);
        }
        // savedRequest가 없고 Referer 헤더가 있으면 Referer URL로 리다이렉트
        else {
            redirectUrl = (String) request.getSession().getAttribute("redirectUrl");
            request.getSession().removeAttribute("redirectUrl");  // URL을 사용 후 세션에서 제거
            redirectStrategy.sendRedirect(request, response, redirectUrl);
        }
    }
}
