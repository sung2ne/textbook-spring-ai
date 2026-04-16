package com.example.springai.controller;

import com.example.springai.service.ChatService;
import com.example.springai.service.ReviewService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

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

    @GetMapping("/review")
    public String review(
            @RequestParam(defaultValue = "시니어 개발자") String role,
            @RequestParam(defaultValue = "Java") String language,
            @RequestParam String code) {
        return reviewService.reviewCode(role, language, code);
    }

    // PART 02 Ch 03: SSE 스트리밍
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> stream(@RequestParam String message) {
        return chatService.streamChat(message);
    }
}
