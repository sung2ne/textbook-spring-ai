package com.example.springai.service;

import com.example.springai.tool.DatabaseTool;
import com.example.springai.tool.WeatherTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

/**
 * PART 04 Ch 03: ReAct 에이전트 패턴 — Reason + Act 반복
 */
@Service
public class ReActAgent {

    private final ChatClient chatClient;

    // CalendarTool: Ch 02의 @Tool 패턴으로 직접 구현 가능 (여기서는 WeatherTool + DatabaseTool 사용)
    public ReActAgent(ChatClient.Builder builder,
                      WeatherTool weatherTool,
                      DatabaseTool databaseTool) {
        this.chatClient = builder
                .defaultSystem("""
                        당신은 사용자를 돕는 AI 에이전트입니다.
                        질문을 해결하기 위해 필요한 도구를 순차적으로 사용하세요.
                        각 단계에서 어떤 도구를 왜 사용하는지 설명하세요.
                        """)
                .defaultTools(weatherTool, databaseTool)
                .build();
    }

    public String run(String goal) {
        return chatClient.prompt().user(goal).call().content();
    }
}
