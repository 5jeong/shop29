package com.toy2.shop29.order.interceptor;

import com.toy2.shop29.order.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class OrderSessionInterceptor implements HandlerInterceptor {

    private final OrderService orderService;

    public OrderSessionInterceptor(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("loginUser");
        String tid = (String) session.getAttribute("tid");
        if (userId == null) {
            response.sendRedirect("/");
        }
        if (tid != null) {
            orderService.deleteOrderHistory(userId, tid);
            session.removeAttribute("tid");
        }
        return true;
    }
}