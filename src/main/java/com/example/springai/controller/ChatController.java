package com.example.springai.controller;

import com.example.springai.service.ChatService;
import com.example.springai.service.ReviewService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;
    private final ReviewService reviewService;

    public ChatController(ChatService chatService, ReviewService reviewService) {
        this.chatService = chatService;
        this.reviewService = reviewService;
    }

    @GetMapping
    public String chat(@RequestParam String message) {
        return chatService.chat(message);
    }

    @GetMapping("/recommend")
    public ChatService.BookRecommendation recommend(@RequestParam String topic) {
        return chatService.recommend(topic);
    }

    @GetMapping("/conversation")
    public String conversation(
            @RequestParam String sessionId,
            @RequestParam String message) {
        return chatService.conversationChat(sessionId, message);
    }

    // PART 02 Ch 02: 프롬프트 템플릿
    @GetMapping("/review")
    public String review(
            @RequestParam(defaultValue = "시니어 개발자") String role,
            @RequestParam(defaultValue = "Java") String language,
            @RequestParam String code) {
        return reviewService.reviewCode(role, language, code);
    }
}
