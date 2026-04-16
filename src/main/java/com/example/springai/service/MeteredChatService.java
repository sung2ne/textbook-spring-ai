package com.example.springai.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Usage;
import org.springframework.stereotype.Service;

/**
 * PART 05 Ch 03: Observability — 토큰 사용량 및 응답 시간 메트릭 수집
 */
@Slf4j
@Service
public class MeteredChatService {

    private final ChatClient chatClient;
    private final MeterRegistry meterRegistry;

    public MeteredChatService(ChatClient chatClient, MeterRegistry meterRegistry) {
        this.chatClient = chatClient;
        this.meterRegistry = meterRegistry;
    }

    public String chat(String userId, String message) {
        Timer.Sample sample = Timer.start(meterRegistry);

        try {
            ChatResponse response = chatClient
                    .prompt()
                    .user(message)
                    .call()
                    .chatResponse();

            Usage usage = response.getMetadata().getUsage();

            // 토큰 사용량 메트릭 기록
            Counter.builder("ai.tokens.input")
                    .tag("user_id", userId)
                    .register(meterRegistry)
                    .increment(usage.getPromptTokens());
            Counter.builder("ai.tokens.output")
                    .tag("user_id", userId)
                    .register(meterRegistry)
                    .increment(usage.getGenerationTokens());

            return response.getResult().getOutput().getText();

        } finally {
            sample.stop(Timer.builder("ai.request.duration")
                    .tag("user_id", userId)
                    .register(meterRegistry));
        }
    }
}
