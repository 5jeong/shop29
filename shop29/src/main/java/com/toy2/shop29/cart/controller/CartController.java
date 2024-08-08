package com.toy2.shop29.cart.controller;

import com.toy2.shop29.cart.service.CartService;
import com.toy2.shop29.cart.service.CartServiceImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.UUID;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping("/get-list")
    public String getCartList(HttpServletRequest request, HttpServletResponse response, Model m, @SessionAttribute(name = "userId", required = false) String userId) {
        try {
            // 회원 확인 및 쿠키 남아있는지 확인
            // 회원이면서 쿠키에 비회원 데이터가 남아있지않으면 패스
            // 회원이면서 쿠키에 비회원 데이터가 남아있다면 비회원 때 추가한 회원 장바구니에 추가한다.

            // if (userId == null) {
            //     HttpSession session = request.getSession(true);
            //     session.setAttribute("userId", "123");
            //     // Session의 유효 시간 설정 (1800초 = 30분)
            //     session.setMaxInactiveInterval(1800);
            //     Cookie cartCookie = new Cookie("cartCookie", ckId);
            //     cartCookie.setPath("/");
            //     cartCookie.setMaxAge(60 * 60 * 24);
            //     response.addCookie(cartCookie);
            // }

            HttpSession session = request.getSession(true);
            m.addAttribute("cartList", cartService.getAllCart("user001"));
            return "cart";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
