package com.example.springai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * PART 01~02: ChatClient 핵심 사용 예제
 */
@Service
public class ChatService {

    private final ChatClient chatClient;
    private final ChatClient memoryChatClient;

    public ChatService(ChatClient.Builder builder, ChatMemory chatMemory) {
        // 기본 ChatClient
        this.chatClient = builder.build();

        // 메모리가 적용된 ChatClient
        this.memoryChatClient = builder
                .defaultAdvisors(
                    MessageChatMemoryAdvisor.builder(chatMemory).build()
                )
                .build();
    }

    /**
     * PART 01 Ch 04: 기본 텍스트 응답
     */
    public String chat(String message) {
        return chatClient
                .prompt()
                .user(message)
                .call()
                .content();
    }

    /**
     * PART 02 Ch 01: 대화 히스토리 유지
     */
    public String conversationChat(String conversationId, String message) {
        return memoryChatClient
                .prompt()
                .user(message)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                .call()
                .content();
    }

    /**
     * PART 02 Ch 03: 스트리밍 응답
     */
    public Flux<String> streamChat(String message) {
        return chatClient
                .prompt()
                .user(message)
                .stream()
                .content();
    }
}
