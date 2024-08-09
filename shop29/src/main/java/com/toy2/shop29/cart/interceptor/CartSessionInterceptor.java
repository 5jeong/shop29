package com.toy2.shop29.cart.interceptor;

import com.toy2.shop29.cart.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

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
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            HttpSession session = request.getSession(true);
            String userId = (String) session.getAttribute("userId");
            String guestId = (String) session.getAttribute("guestId");
            //
            if (userId == null && guestId == null) {
                String uniqueId = generateId();
                session.setAttribute("guestId", uniqueId);
                session.setAttribute("is_user", 0);
                session.setMaxInactiveInterval(1800);
            }
            if (userId != null && guestId != null) {
                cartService.updateGuestCartToUser(userId, guestId, 1);
            }

        } catch (Exception e) {
            HttpSession session = request.getSession(true);
            session.invalidate();
        }
        return true;
    }
}