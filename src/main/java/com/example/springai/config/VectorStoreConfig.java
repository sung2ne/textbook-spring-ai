package com.example.springai.config;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class VectorStoreConfig {

    // 프로덕션: PgVectorStore
    @Bean
    @Profile("prod")
    public VectorStore pgVectorStore(JdbcTemplate jdbcTemplate, EmbeddingModel embeddingModel) {
        return PgVectorStore.builder(jdbcTemplate, embeddingModel)
                .dimensions(1536)
                .initializeSchema(true)
                .build();
    }

    // 개발: SimpleVectorStore (인메모리, 의존성 없음)
    @Bean
    @Profile("dev")
    public VectorStore simpleVectorStore(EmbeddingModel embeddingModel) {
        return org.springframework.ai.vectorstore.SimpleVectorStore
                .builder(embeddingModel)
                .build();
    }
}
