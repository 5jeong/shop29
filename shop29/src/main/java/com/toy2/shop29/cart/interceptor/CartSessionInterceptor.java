package com.toy2.shop29.cart.interceptor;

import com.toy2.shop29.cart.service.CartService;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@Component
public class CartSessionInterceptor implements HandlerInterceptor {

    private final CartService cartService;

    public CartSessionInterceptor(CartService cartService) {
        this.cartService = cartService;
    }

    public static String generateId() {
        // 현재 날짜와 시간을 YYMMDDhhmmss 형식으로 포맷팅
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
        String dateStr = sdf.format(new Date());

        // 8자리 랜덤 숫자 생성
        Random random = new Random();
        int randomNumber = random.nextInt(100000000); // 0 ~ 99999999 사이의 랜덤 숫자 생성
        String randomStr = String.format("%08d", randomNumber); // 8자리로 포맷팅 (부족한 자리수는 0으로 채움)

        // 두 문자열 결합
        return dateStr + "_" + randomStr;
    }

    @Override
    public boolean preHandle(@Nullable HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        try {
            HttpSession session = request.getSession(true);
            // TODO : 임시아이디
            session.setAttribute("loginUser", "user008");

            String userId = (String) session.getAttribute("loginUser");
            String guestId = null;

            // 비로그인 연장
            if (request.getCookies() != null) {
                for (Cookie cookie : request.getCookies()) {
                    if ("guestId".equals(cookie.getName())) {
                        guestId = cookie.getValue();
                        cookie.setMaxAge(60 * 60 * 24); // 쿠키 유효 기간: 1일
                        cookie.setHttpOnly(true); // XSS 공격 방지
                        cookie.setPath("/"); // 쿠키의 적용 경로 설정 (전체 사이트에 적용)
                        response.addCookie(cookie); // 브라우저에 쿠키 저장

                        // 쿠키의 유효기간을 초기화하여 연장
                        cookie.setMaxAge(60 * 60 * 24); // 쿠키 유효 기간: 1일
                        response.addCookie(cookie); // 쿠키를 다시 설정하여 유효기간 연장
                        break;
                    }
                }
            }

            if (userId == null && guestId == null) {
                String uniqueId = generateId();
                Cookie cookie = new Cookie("guestId", uniqueId);
                cookie.setMaxAge(60 * 60 * 24); // 쿠키 유효 기간: 1일
                cookie.setHttpOnly(true); // XSS 공격 방지
                cookie.setPath("/"); // 쿠키의 적용 경로 설정 (전체 사이트에 적용)
                response.addCookie(cookie); // 브라우저에 쿠키 저장
            }
            if (userId != null && guestId != null) {
                cartService.updateGuestCartToUser(userId, guestId, 1);
            }

        } catch (Exception e) {
            HttpSession session = request.getSession(true);
            session.invalidate();
            Cookie myCookie = new Cookie("guestId", null);
            myCookie.setMaxAge(0); // 쿠키의 expiration 타임을 0으로 하여 없앤다.
            myCookie.setPath("/"); // 모든 경로에서 삭제 됬음을 알린다.
            response.addCookie(myCookie);
        }
        return true;
    }
}