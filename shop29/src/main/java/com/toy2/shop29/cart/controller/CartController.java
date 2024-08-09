package com.toy2.shop29.cart.controller;

import com.toy2.shop29.cart.domain.CartDto;
import com.toy2.shop29.cart.domain.request.AddCartProductDto;
import com.toy2.shop29.cart.domain.request.OrderCountRequestDto;
import com.toy2.shop29.cart.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping("/get-list")
    public String getCartList(HttpServletRequest request, HttpServletResponse response, Model m, @SessionAttribute(name = "userId", required = false) String userId, @SessionAttribute(name = "guestId", required = false) String guestId) {
        String userInfo = getUserInfo(userId, guestId);
        Integer isUser = (userId != null) ? 1 : 0;
        try {
            List<CartDto> getAllCart = cartService.getUserCartProducts(userInfo, isUser);
            m.addAttribute("cartList", getAllCart);
            return "cart";
        } catch (Exception e) {
            e.printStackTrace();
            return "cart";
        }
    }

    @PostMapping("/cart-item")
    public String addCartItem(@SessionAttribute(name = "userId", required = false) String userId, @SessionAttribute(name = "guestId", required = false) String guestId, @RequestBody AddCartProductDto AddCartProductDto) {
        String userInfo = getUserInfo(userId, guestId);
        Integer isUser = (userId != null) ? 1 : 0;
        try {
            cartService.addProductToCart(userInfo, AddCartProductDto.productId, AddCartProductDto.qauntity, isUser);
        } catch (Exception e) {
            System.out.println(e);
        }
        return "redirect:/cart/get-list";
    }

    @PostMapping("/order-count")
    public @ResponseBody HashMap<String, String> orderCount(@RequestBody OrderCountRequestDto orderCountRequestDto, @SessionAttribute(name = "userId", required = false) String userId, @SessionAttribute(name = "guestId", required = false) String guestId) {
        int product_id = orderCountRequestDto.getProduct_id();
        int quantity = orderCountRequestDto.getQuantity();
        String userInfo = getUserInfo(userId, guestId);

        if (quantity > 100) {
            HashMap<String, String> map = new HashMap<>();
            map.put("status", "fail");
            map.put("message", "");
            return map;
        }

        try {
            cartService.updateProductQuantity(userInfo, product_id, quantity);
            HashMap<String, String> map = new HashMap<>();
            map.put("status", "success");
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            HashMap<String, String> map = new HashMap<>();
            map.put("status", "fail");
            map.put("message", e.getMessage());
            return map;
        }
    }

    public String getUserInfo(String userId, String guestId) {
        return (userId != null) ? userId : guestId;
    }
}
