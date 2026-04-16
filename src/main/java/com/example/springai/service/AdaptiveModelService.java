package com.example.springai.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.stereotype.Service;

/**
 * PART 02 Ch 04: 모델 선택 전략
 * 복잡도에 따라 gpt-4o-mini(일반) 또는 gpt-4o(복잡)을 동적으로 선택합니다.
 */
@Slf4j
@Service
public class AdaptiveModelService {

    private final ChatClient fastClient;    // gpt-4o-mini (저비용)
    private final ChatClient powerClient;   // gpt-4o (고성능)

    public AdaptiveModelService(ChatClient.Builder builder) {
        this.fastClient = builder
                .defaultOptions(OpenAiChatOptions.builder()
                        .model("gpt-4o-mini")
                        .build())
                .build();

        this.powerClient = builder
                .defaultOptions(OpenAiChatOptions.builder()
                        .model("gpt-4o")
                        .build())
                .build();
    }

    public String smartChat(String message) {
        boolean isComplex = message.length() > 500
                || message.contains("분석")
                || message.contains("설계");

        ChatClient client = isComplex ? powerClient : fastClient;
        log.info("모델 선택: {} (메시지 길이: {})",
                isComplex ? "gpt-4o" : "gpt-4o-mini", message.length());

        return client.prompt().user(message).call().content();
    }
}
