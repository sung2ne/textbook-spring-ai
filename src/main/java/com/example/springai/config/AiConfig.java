package com.example.springai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    /**
     * PART 01 Ch 04: 기본 ChatClient
     * defaultSystem으로 AI 역할을 고정합니다.
     */
    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder
                .defaultSystem("당신은 친절하고 유용한 AI 어시스턴트입니다.")
                .build();
    }

    /**
     * PART 02 Ch 01: 대화 메모리
     * MessageWindowChatMemory는 최근 N개의 메시지만 유지합니다.
     */
    @Bean
    public ChatMemory chatMemory() {
        return MessageWindowChatMemory.builder()
                .maxMessages(20)
                .build();
    }
}
