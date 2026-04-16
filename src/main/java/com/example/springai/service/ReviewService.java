package com.example.springai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

/**
 * PART 02 Ch 02: 프롬프트 템플릿 활용
 */
@Service
public class ReviewService {

    private final ChatClient chatClient;

    @Value("classpath:prompts/code-review.st")
    private Resource reviewPromptResource;

    public ReviewService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public String reviewCode(String role, String language, String code) {
        return chatClient
                .prompt()
                .user(u -> u.text(reviewPromptResource)
                            .param("role", role)
                            .param("language", language)
                            .param("code", code))
                .call()
                .content();
    }
}
