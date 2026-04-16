package com.example.springai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.evaluation.EvaluationResponse;
import org.springframework.ai.evaluation.RelevancyEvaluator;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * PART 03 Ch 05: RAG 성능 평가
 */
@Component
public class RagEvaluator {

    record EvaluationResult(boolean pass, double score, String feedback) {}

    private final RelevancyEvaluator relevancyEvaluator;

    public RagEvaluator(ChatClient.Builder builder) {
        this.relevancyEvaluator = new RelevancyEvaluator(builder.build());
    }

    public EvaluationResult evaluate(String question, String answer,
                                     List<org.springframework.ai.document.Document> contexts) {
        EvaluationRequest request = new EvaluationRequest(question, contexts, answer);
        EvaluationResponse response = relevancyEvaluator.evaluate(request);
        return new EvaluationResult(response.isPass(), response.getScore(), response.getFeedback());
    }
}
