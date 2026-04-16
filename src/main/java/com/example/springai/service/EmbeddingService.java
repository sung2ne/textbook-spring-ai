package com.example.springai.service;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Service;

/**
 * PART 03 Ch 01: 임베딩과 벡터 공간 이해
 */
@Service
public class EmbeddingService {

    private final EmbeddingModel embeddingModel;

    public EmbeddingService(EmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
    }

    public float[] embed(String text) {
        return embeddingModel.embed(text);
    }

    public double similarity(String text1, String text2) {
        float[] vec1 = embeddingModel.embed(text1);
        float[] vec2 = embeddingModel.embed(text2);
        return cosineSimilarity(vec1, vec2);
    }

    private double cosineSimilarity(float[] vec1, float[] vec2) {
        double dot = 0, norm1 = 0, norm2 = 0;
        for (int i = 0; i < vec1.length; i++) {
            dot += vec1[i] * vec2[i];
            norm1 += vec1[i] * vec1[i];
            norm2 += vec2[i] * vec2[i];
        }
        return dot / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }
}
