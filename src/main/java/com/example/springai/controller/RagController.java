package com.example.springai.controller;

import com.example.springai.service.DocumentIngestionService;
import com.example.springai.service.RagService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/rag")
public class RagController {

    private final RagService ragService;
    private final DocumentIngestionService ingestionService;

    public RagController(RagService ragService, DocumentIngestionService ingestionService) {
        this.ragService = ragService;
        this.ingestionService = ingestionService;
    }

    @PostMapping("/ingest")
    public ResponseEntity<String> ingest(@RequestParam MultipartFile file) throws IOException {
        ingestionService.ingest(file);
        return ResponseEntity.accepted().body("문서를 처리 중입니다.");
    }

    @GetMapping("/ask")
    public RagService.RagResponse ask(@RequestParam String question) {
        return ragService.askWithSources(question);
    }
}
