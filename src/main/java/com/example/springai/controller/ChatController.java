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

    // PART 01 Ch 04: 구조화된 응답
    @GetMapping("/recommend")
    public ChatService.BookRecommendation recommend(@RequestParam String topic) {
        return chatService.recommend(topic);
    }
}
