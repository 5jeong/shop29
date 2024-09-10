package com.toy2.shop29.order.interceptor;

import com.toy2.shop29.order.service.OrderService;
import com.toy2.shop29.users.domain.UserContext;
import com.toy2.shop29.users.domain.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class OrderSessionInterceptor implements HandlerInterceptor {
    private final OrderService orderService;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HttpSession session = request.getSession();

        Authentication authentication = SecurityContextHolder.getContextHolderStrategy().getContext()
                .getAuthentication();

        UserContext userContext = (UserContext) authentication.getPrincipal();
        UserDto userDto = userContext.getUserDto();
        String userId = userDto.getUserId();
        String tid = (String) session.getAttribute("tid");

        if (tid != null) {
            orderService.deleteOrderHistory(userId, tid);
            session.removeAttribute("tid");
        }

        return true;
    }
}