package com.example.springai.controller;

import com.example.springai.tool.DatabaseTool;
import com.example.springai.tool.WeatherTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/agent")
public class AgentController {

    private final ChatClient chatClient;
    private final WeatherTool weatherTool;
    private final DatabaseTool databaseTool;

    public AgentController(ChatClient chatClient, WeatherTool weatherTool, DatabaseTool databaseTool) {
        this.chatClient = chatClient;
        this.weatherTool = weatherTool;
        this.databaseTool = databaseTool;
    }

    @GetMapping("/weather")
    public String weatherChat(@RequestParam String message) {
        return chatClient
                .prompt()
                .system("날씨 관련 질문에는 반드시 getWeather 도구를 사용하여 실시간 데이터를 조회하세요.")
                .user(message)
                .tools(weatherTool)
                .call()
                .content();
    }

    // PART 04 Ch 02: 도구 등록과 실행
    @GetMapping("/search")
    public String searchChat(@RequestParam String message) {
        return chatClient
                .prompt()
                .user(message)
                .tools(databaseTool)
                .call()
                .content();
    }
}
