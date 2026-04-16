package com.example.springai.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.stereotype.Service;

/**
 * PART 06 Ch 03: DALL-E 이미지 생성 서비스
 */
@Slf4j
@Service
public class ImageGenerationService {

    private final ImageModel imageModel;
    private final ChatClient translatorClient;

    public ImageGenerationService(ImageModel imageModel, ChatClient.Builder builder) {
        this.imageModel = imageModel;
        this.translatorClient = builder
                .defaultSystem("""
                        이미지 생성 AI를 위한 프롬프트 전문가입니다.
                        한국어 설명을 DALL-E에 최적화된 영어 프롬프트로 변환합니다.
                        세부 묘사, 스타일, 조명을 포함합니다. 50단어 이내로 작성합니다.
                        """)
                .build();
    }

    public ImageGenerationResult generate(String koreanPrompt) {
        // 1단계: 한국어 → 영문 프롬프트 최적화
        String englishPrompt = translatorClient
                .prompt()
                .user(koreanPrompt)
                .call()
                .content();

        // 2단계: 이미지 생성
        ImageResponse response = imageModel.call(
                new ImagePrompt(englishPrompt,
                        OpenAiImageOptions.builder()
                                .model("dall-e-3")
                                .quality("standard")
                                .size("1024x1024")
                                .build()
                )
        );

        String imageUrl = response.getResult().getOutput().getUrl();
        String revisedPrompt = response.getResult().getOutput().getRevisedPrompt();

        log.info("이미지 생성 완료. URL: {}", imageUrl);
        return new ImageGenerationResult(imageUrl, englishPrompt, revisedPrompt);
    }

    public record ImageGenerationResult(String imageUrl, String usedPrompt, String revisedPrompt) {}
}
