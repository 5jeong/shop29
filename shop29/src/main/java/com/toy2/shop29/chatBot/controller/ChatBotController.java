package com.toy2.shop29.chatBot.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.toy2.shop29.chatBot.domain.ChatBotRequestDto;
import com.toy2.shop29.chatBot.service.ChatBotService;
import com.toy2.shop29.users.domain.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chatbot")
public class ChatBotController {
    private final ChatBotService chatBotService;

    @PostMapping("")
    public ChatBotRequestDto chatBot(
            @AuthenticationPrincipal UserContext user,
            @CookieValue(name = "guestId", required = false) String guestId,
            @RequestBody ChatBotRequestDto messageRequest
    ) throws JsonProcessingException {
        String userInfo = getUserInfo(user, guestId);
        String userMessage = messageRequest.getMessage();

        String responseMessage = chatBotService.sendMessage(userInfo, userMessage);
        return new ChatBotRequestDto(responseMessage);
    }

    @GetMapping("/order-history")
    public ChatBotRequestDto requestOrderHistory(
            @AuthenticationPrincipal UserContext user,
            @RequestParam(required = true, name = "id") String orderId
    ) throws JsonProcessingException {
        String userInfo = getUserInfo(user, "");
        String responseMessage = chatBotService.getOrderHistory(userInfo, orderId);
        return new ChatBotRequestDto(responseMessage);
        // return new ChatBotRequestDto(responseMessage);
    }

    @GetMapping("/refund")
    public ChatBotRequestDto getRefundableOrder(
            @AuthenticationPrincipal UserContext user,
            @RequestParam(required = false, name = "orderId") String orderId
    ) throws JsonProcessingException {
        String userInfo = getUserInfo(user, "");
        if (orderId == null) {
            String responseMessage = chatBotService.getRefundableOrderList(userInfo);
            return new ChatBotRequestDto(responseMessage);
        }
        String responseMessage = chatBotService.getRefundableOrder(userInfo, orderId);
        return new ChatBotRequestDto(responseMessage);
    }

    @GetMapping("/product")
    public ChatBotRequestDto getProduct(
            @RequestParam(required = false, name = "productId") String productId
    ) throws JsonProcessingException {
        if (productId == null) {
            String responseMessage = chatBotService.getProductSearcher();
            return new ChatBotRequestDto(responseMessage);
        }
        String responseMessage = chatBotService.getProductInformation(productId);
        return new ChatBotRequestDto(responseMessage);
    }

    // 로그인 비로그인 검증
    public String getUserInfo(UserContext user, String guestId) {
        return (user != null) ? user.getUserDto().getUserId() : guestId;
    }
}
