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
 * PART 03 Ch 04: RAG 파이프라인 구현
 */
@Slf4j
@Service
public class RagService {

    private final ChatClient ragChatClient;

    public RagService(ChatClient.Builder builder, VectorStore vectorStore) {
        this.ragChatClient = builder
                .defaultSystem("""
                        당신은 제공된 문서 내용을 바탕으로 질문에 답합니다.
                        문서에 없는 내용은 모른다고 답하세요.
                        """)
                .defaultAdvisors(
                    QuestionAnswerAdvisor.builder(vectorStore)
                            .searchRequest(SearchRequest.builder()
                                    .topK(5)
                                    .similarityThreshold(0.65)
                                    .build())
                            .build()
                )
                .build();
    }

    public String ask(String question) {
        return ragChatClient.prompt().user(question).call().content();
    }

    public RagResponse askWithSources(String question) {
        ChatResponse response = ragChatClient
                .prompt()
                .user(question)
                .call()
                .chatResponse();

        List<Document> retrievedDocs = (List<Document>) response
                .getMetadata()
                .get(QuestionAnswerAdvisor.RETRIEVED_DOCUMENTS);

        List<String> sources = retrievedDocs == null ? List.of() :
                retrievedDocs.stream()
                        .map(doc -> (String) doc.getMetadata().get("source"))
                        .filter(Objects::nonNull)
                        .distinct()
                        .toList();

        return new RagResponse(response.getResult().getOutput().getText(), sources);
    }

    public record RagResponse(String answer, List<String> sources) {}
}
