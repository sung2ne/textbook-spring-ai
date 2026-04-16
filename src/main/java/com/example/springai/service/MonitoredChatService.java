package com.example.springai.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Usage;
import org.springframework.stereotype.Service;

/**
 * PART 01 Ch 05: 토큰 사용량 모니터링
 */
@Slf4j
@Service
public class MonitoredChatService {

    private final ChatClient chatClient;

    public MonitoredChatService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public String chat(String message) {
        ChatResponse response = chatClient
                .prompt()
                .user(message)
                .call()
                .chatResponse();

        Usage usage = response.getMetadata().getUsage();
        log.info("토큰 사용량 — 입력: {}, 출력: {}, 합계: {}",
                usage.getPromptTokens(),
                usage.getGenerationTokens(),
                usage.getTotalTokens());

        return response.getResult().getOutput().getText();
    }
}
