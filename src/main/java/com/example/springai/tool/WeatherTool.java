package com.example.springai.tool;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

/**
 * PART 04 Ch 01: Function Calling — 날씨 조회 도구
 */
@Component
public class WeatherTool {

    @Tool(description = "현재 날씨를 조회합니다. city 파라미터에 도시명(한국어 가능)을 입력하세요.")
    public WeatherInfo getWeather(
            @ToolParam(description = "도시명 (예: 서울, 부산)") String city) {
        // 실제 날씨 API 호출 대신 더미 데이터 반환 (실습용)
        return new WeatherInfo(city, "맑음", 22, 45);
    }

    public record WeatherInfo(String city, String condition, int temperature, int humidity) {}
}
