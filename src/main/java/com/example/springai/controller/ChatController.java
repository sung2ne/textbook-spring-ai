package com.example.springai.controller;

import com.example.springai.service.ChatService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * PART 01 Ch 04 / PART 02 Ch 03
 * 기본 채팅 + SSE 스트리밍 엔드포인트
 */
@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    /**
     * PART 01 Ch 04: 기본 채팅
     * GET /api/chat?message=안녕하세요
     */
    @GetMapping
    public String chat(@RequestParam String message) {
        return chatService.chat(message);
    }

    /**
     * PART 02 Ch 01: 대화 메모리를 활용한 채팅
     * GET /api/chat/conversation?sessionId=abc&message=안녕
     */
    @GetMapping("/conversation")
    public String conversation(
            @RequestParam String sessionId,
            @RequestParam String message) {
        return chatService.conversationChat(sessionId, message);
    }

    /**
     * PART 02 Ch 03: SSE 스트리밍 응답
     * GET /api/chat/stream?message=안녕하세요
     */
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> stream(@RequestParam String message) {
        return chatService.streamChat(message);
    }
}
