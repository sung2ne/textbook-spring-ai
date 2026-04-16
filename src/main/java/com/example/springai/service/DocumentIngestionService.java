package com.example.springai.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.FileSystemResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * PART 03 Ch 02: 문서 로더와 청킹 전략
 */
@Slf4j
@Service
public class DocumentIngestionService {

    private final VectorStore vectorStore;
    private final TokenTextSplitter splitter = new TokenTextSplitter(512, 128, 5, 10000, true);

    public DocumentIngestionService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @Async
    public CompletableFuture<Void> ingest(MultipartFile file) throws IOException {
        Path tempPath = Files.createTempFile("upload-", "-" + file.getOriginalFilename());
        file.transferTo(tempPath);

        PagePdfDocumentReader reader = new PagePdfDocumentReader(
                new FileSystemResource(tempPath));

        List<Document> chunks = splitter.apply(reader.get());
        chunks.forEach(doc -> doc.getMetadata().putAll(Map.of(
                "source", file.getOriginalFilename(),
                "document_id", UUID.randomUUID().toString(),
                "ingested_at", Instant.now().toString()
        )));

        vectorStore.add(chunks);
        Files.deleteIfExists(tempPath);

        log.info("문서 수집 완료: {} — {}개 청크", file.getOriginalFilename(), chunks.size());
        return CompletableFuture.completedFuture(null);
    }
}
