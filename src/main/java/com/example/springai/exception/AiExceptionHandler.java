package com.example.springai.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.retry.NonTransientAiException;
import org.springframework.ai.retry.TransientAiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * PART 02 Ch 05: LLM API 전역 예외 핸들러
 */
@Slf4j
@RestControllerAdvice
public class AiExceptionHandler {

    record ErrorResponse(String message) {}

    // 재시도 후에도 실패한 일시적 오류 (Rate Limit, 서버 오류)
    @ExceptionHandler(TransientAiException.class)
    public ResponseEntity<ErrorResponse> handleTransient(TransientAiException e) {
        log.warn("AI 일시적 오류 (재시도 소진): {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(new ErrorResponse("AI 서비스가 일시적으로 불안정합니다. 잠시 후 다시 시도해주세요."));
    }

    // 재시도 불가 오류 (잘못된 API 키, 잘못된 요청)
    @ExceptionHandler(NonTransientAiException.class)
    public ResponseEntity<ErrorResponse> handleNonTransient(NonTransientAiException e) {
        log.error("AI 비재시도 오류 — 즉시 확인 필요: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("AI 서비스 오류가 발생했습니다."));
    }
}
