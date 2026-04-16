package com.example.springai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final ChatClient chatClient;
    private final ChatClient memoryChatClient;

    public ChatService(
            ChatClient chatClient,
            @Qualifier("memoryChatClient") ChatClient memoryChatClient) {
        this.chatClient = chatClient;
        this.memoryChatClient = memoryChatClient;
    }

    public String chat(String message) {
        return chatClient
                .prompt()
                .user(message)
                .call()
                .content();
    }

    public BookRecommendation recommend(String topic) {
        return chatClient
                .prompt()
                .user("Java " + topic + " 관련 책 한 권을 추천해주세요.")
                .call()
                .entity(BookRecommendation.class);
    }

    // PART 02 Ch 01: 대화 히스토리 유지
    public String conversationChat(String conversationId, String message) {
        return memoryChatClient
                .prompt()
                .user(message)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                .call()
                .content();
    }

    public record BookRecommendation(String title, String author, String reason) {}
}
