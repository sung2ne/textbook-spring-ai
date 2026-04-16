package com.example.springai.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * PART 05 Ch 04: 시맨틱 캐싱 — 유사한 질문에 대해 LLM 호출 없이 캐시 응답 반환
 */
@Slf4j
@Service
public class SemanticCacheService {

    private final ChatClient chatClient;
    private final VectorStore cacheStore;

    private static final double CACHE_HIT_THRESHOLD = 0.95;

    public SemanticCacheService(ChatClient chatClient, VectorStore cacheStore) {
        this.chatClient = chatClient;
        this.cacheStore = cacheStore;
    }

    public String chat(String userMessage) {
        // 유사한 기존 질문 검색
        List<Document> cached = cacheStore.similaritySearch(
                SearchRequest.builder()
                        .query(userMessage)
                        .topK(1)
                        .similarityThreshold(CACHE_HIT_THRESHOLD)
                        .build()
        );

        if (!cached.isEmpty()) {
            log.info("캐시 히트: {}", userMessage);
            return (String) cached.get(0).getMetadata().get("answer");
        }

        // 캐시 미스 — LLM 호출
        String answer = chatClient.prompt().user(userMessage).call().content();

        // 캐시 저장
        Document cacheEntry = new Document(userMessage,
                Map.of("answer", answer, "cached_at", Instant.now().toString()));
        cacheStore.add(List.of(cacheEntry));

        return answer;
    }
}
