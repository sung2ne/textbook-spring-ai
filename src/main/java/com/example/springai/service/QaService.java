package com.example.springai.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * PART 06 Ch 02: 사내 문서 QA — 부서별 권한 기반 RAG
 */
@Slf4j
@Service
public class QaService {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;
    private final AccessControlService accessControlService;

    public QaService(ChatClient.Builder builder, VectorStore vectorStore,
                     AccessControlService accessControlService) {
        this.vectorStore = vectorStore;
        this.accessControlService = accessControlService;
        this.chatClient = builder
                .defaultSystem("""
                        당신은 사내 문서 전문가입니다.
                        제공된 문서 내용을 바탕으로 정확하게 답변합니다.
                        문서에 없는 내용은 "관련 내용을 찾을 수 없습니다"라고 답합니다.
                        """)
                .build();
    }

    public QaResponse ask(String question, String department, String userId) {
        if (!accessControlService.canAccess(userId, department)) {
            throw new SecurityException("해당 부서의 문서에 접근 권한이 없습니다.");
        }

        ChatResponse response = chatClient
                .prompt()
                .user(question)
                .advisors(
                    QuestionAnswerAdvisor.builder(vectorStore)
                            .searchRequest(SearchRequest.builder()
                                    .topK(5)
                                    .similarityThreshold(0.65)
                                    .filterExpression("department == '" + department + "'")
                                    .build())
                            .build()
                )
                .call()
                .chatResponse();

        List<Document> docs = (List<Document>) response.getMetadata()
                .get(QuestionAnswerAdvisor.RETRIEVED_DOCUMENTS);

        List<String> sources = docs == null ? List.of() :
                docs.stream()
                        .map(d -> (String) d.getMetadata().get("source"))
                        .filter(Objects::nonNull)
                        .distinct()
                        .toList();

        return new QaResponse(response.getResult().getOutput().getText(), sources);
    }

    public record QaResponse(String answer, List<String> sources) {}
}
