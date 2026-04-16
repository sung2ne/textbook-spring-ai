package com.example.springai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final ChatClient chatClient;

    public ChatService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public String chat(String message) {
        return chatClient
                .prompt()
                .user(message)
                .call()
                .content();
    }

    // PART 01 Ch 04: 구조화된 응답 (entity 사용)
    public BookRecommendation recommend(String topic) {
        return chatClient
                .prompt()
                .user("Java " + topic + " 관련 책 한 권을 추천해주세요.")
                .call()
                .entity(BookRecommendation.class);
    }

    public record BookRecommendation(String title, String author, String reason) {}
}
