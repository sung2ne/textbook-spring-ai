package com.example.springai.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.retry.TransientAiException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Slf4j
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

    @Retryable(
        retryFor = { TransientAiException.class },
        maxAttempts = 3,
        backoff = @Backoff(delay = 1000, multiplier = 2.0, maxDelay = 10000)
    )
    public String chat(String message) {
        return chatClient
                .prompt()
                .user(message)
                .call()
                .content();
    }

    @Recover
    public String recoverChat(TransientAiException e, String message) {
        log.error("AI 호출 3회 모두 실패: {}", e.getMessage());
        return "현재 AI 서비스를 이용할 수 없습니다. 잠시 후 다시 시도해주세요.";
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

    public Flux<String> streamChat(String message) {
        return chatClient
                .prompt()
                .user(message)
                .stream()
                .content();
    }

    public record BookRecommendation(String title, String author, String reason) {}
}
