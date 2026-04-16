package com.example.springai.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * PART 06 Ch 01: 사용자별 일일 사용량 제한
 */
@Slf4j
@Service
public class QuotaService {

    private static final int DAILY_TOKEN_LIMIT = 100_000;
    private final Map<String, AtomicInteger> userTokenUsage = new ConcurrentHashMap<>();

    public void checkAndRecord(String userId, int tokensUsed) {
        AtomicInteger usage = userTokenUsage.computeIfAbsent(userId, k -> new AtomicInteger(0));
        int total = usage.addAndGet(tokensUsed);

        if (total > DAILY_TOKEN_LIMIT) {
            throw new QuotaExceededException("일일 사용량을 초과했습니다. (userId=" + userId + ")");
        }

        log.debug("토큰 사용량: userId={}, 오늘={}/{}", userId, total, DAILY_TOKEN_LIMIT);
    }

    public int getTodayUsage(String userId) {
        return userTokenUsage.getOrDefault(userId, new AtomicInteger(0)).get();
    }

    public static class QuotaExceededException extends RuntimeException {
        public QuotaExceededException(String message) {
            super(message);
        }
    }
}
