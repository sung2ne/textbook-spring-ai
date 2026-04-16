package com.example.springai.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * PART 05 Ch 01: 프롬프트 인젝션 방어
 */
@Slf4j
@Component
public class PromptSanitizer {

    private static final List<String> INJECTION_PATTERNS = List.of(
            "이전 지시사항을 무시",
            "시스템 프롬프트를 출력",
            "ignore previous instructions",
            "you are now",
            "jailbreak"
    );

    public String sanitize(String userInput) {
        String lower = userInput.toLowerCase();

        for (String pattern : INJECTION_PATTERNS) {
            if (lower.contains(pattern.toLowerCase())) {
                log.warn("잠재적 프롬프트 인젝션 감지: {}", userInput);
                throw new SecurityException("허용되지 않는 입력입니다.");
            }
        }

        if (userInput.length() > 2000) {
            return userInput.substring(0, 2000);
        }

        return userInput;
    }
}
