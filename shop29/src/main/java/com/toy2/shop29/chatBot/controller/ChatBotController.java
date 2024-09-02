package com.toy2.shop29.chatBot.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.toy2.shop29.chatBot.domain.ChatBotRequestDto;
import com.toy2.shop29.chatBot.service.ChatBotService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ChatBotController {
    private final ChatBotService chatBotService;

    @PostMapping("/chatbot")
    public ChatBotRequestDto chatBot(
            @SessionAttribute(name = "loginUser", required = false) String userId,
            @CookieValue(name = "guestId", required = false) String guestId,
            @RequestBody ChatBotRequestDto messageRequest
            ) throws JsonProcessingException {
        String userInfo = getUserInfo(userId, guestId);
        String userMessage = messageRequest.getMessage();

        String responseMessage = chatBotService.sendMessage(userInfo, userMessage);
        return new ChatBotRequestDto(responseMessage);
    }

    // 로그인 비로그인 검증
    public String getUserInfo(String userId, String guestId) {
        return (userId != null) ? userId : guestId;
    }
}
