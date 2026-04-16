package com.example.springai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

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

    public String conversationChat(String conversationId, String message) {
        return memoryChatClient
                .prompt()
                .user(message)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                .call()
                .content();
    }

    // PART 02 Ch 03: 스트리밍 응답
    public Flux<String> streamChat(String message) {
        return chatClient
                .prompt()
                .user(message)
                .stream()
                .content();
    }

    public record BookRecommendation(String title, String author, String reason) {}
}
