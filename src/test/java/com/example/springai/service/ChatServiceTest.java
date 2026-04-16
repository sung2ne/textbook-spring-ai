package com.example.springai.service;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class ChatServiceTest {

    @MockBean
    private ChatModel chatModel;

    @Autowired
    private ChatService chatService;

    @Test
    void 기본_채팅_응답() {
        // Given
        String expectedResponse = "안녕하세요! 무엇을 도와드릴까요?";
        ChatResponse mockResponse = ChatResponse.builder()
                .withGenerations(List.of(
                    new Generation(new AssistantMessage(expectedResponse))
                ))
                .build();
        when(chatModel.call(any(Prompt.class))).thenReturn(mockResponse);

        // When
        String actual = chatService.chat("안녕");

        // Then
        assertThat(actual).isEqualTo(expectedResponse);
    }

    @Test
    void TransientAiException_발생시_폴백_응답() {
        // Given
        when(chatModel.call(any(Prompt.class)))
                .thenThrow(new org.springframework.ai.retry.TransientAiException("Rate limit"));

        // When
        String actual = chatService.chat("질문");

        // Then
        assertThat(actual).contains("잠시 후 다시 시도");
    }
}
