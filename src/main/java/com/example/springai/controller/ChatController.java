package com.example.springai.controller;

import com.example.springai.service.ChatService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping
    public String chat(@RequestParam String message) {
        return chatService.chat(message);
    }

    @GetMapping("/recommend")
    public ChatService.BookRecommendation recommend(@RequestParam String topic) {
        return chatService.recommend(topic);
    }

    // PART 02 Ch 01: 대화 메모리 활용
    @GetMapping("/conversation")
    public String conversation(
            @RequestParam String sessionId,
            @RequestParam String message) {
        return chatService.conversationChat(sessionId, message);
    }
}
