package com.example.springai.controller;

import com.example.springai.service.DocumentIngestionService;
import com.example.springai.service.QaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentIngestionService ingestionService;
    private final QaService qaService;

    public DocumentController(DocumentIngestionService ingestionService, QaService qaService) {
        this.ingestionService = ingestionService;
        this.qaService = qaService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> upload(
            @RequestParam MultipartFile file,
            @RequestParam(defaultValue = "public") String department) throws IOException {
        ingestionService.ingest(file);
        return ResponseEntity.accepted().body("문서를 처리 중입니다.");
    }

    @GetMapping("/qa")
    public QaService.QaResponse qa(
            @RequestParam String question,
            @RequestParam(defaultValue = "public") String department,
            @RequestParam(defaultValue = "user1") String userId) {
        return qaService.ask(question, department, userId);
    }
}
