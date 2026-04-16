# 실전 Spring AI — 예제 코드

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5-brightgreen)](https://spring.io/projects/spring-boot)
[![Spring AI](https://img.shields.io/badge/Spring%20AI-1.0-blue)](https://spring.io/projects/spring-ai)
[![Java](https://img.shields.io/badge/Java-21-orange)](https://openjdk.org/projects/jdk/21/)

> 교재 **실전 Spring AI** (text.ibetter.kr) 의 챕터별 예제 코드 저장소입니다.

---

## 브랜치 구조

각 브랜치는 해당 챕터까지의 누적 코드를 담고 있습니다.  
이전 챕터의 코드가 다음 챕터에도 포함되어, 순서대로 체크아웃하면 코드가 발전하는 과정을 확인할 수 있습니다.

```
main                     ← 최종 완성 코드
│
├── part01/chapter-01    ← 프로젝트 초기화 + 첫 AI 호출
├── part01/chapter-02    ← Spring AI 아키텍처 이해
├── part01/chapter-03    ← 개발 환경 설정 (application.yml)
├── part01/chapter-04    ← ChatClient 기본 사용
├── part01/chapter-05    ← API 키 관리 + 비용 이해
│
├── part02/chapter-01    ← ChatClient API 고급 + 대화 메모리
├── part02/chapter-02    ← 프롬프트 템플릿
├── part02/chapter-03    ← SSE 스트리밍 응답
├── part02/chapter-04    ← 모델 선택 전략
├── part02/chapter-05    ← 에러 핸들링 + 재시도
│
├── part03/chapter-01    ← 임베딩 서비스
├── part03/chapter-02    ← 문서 로더 + 청킹
├── part03/chapter-03    ← PgVectorStore 설정
├── part03/chapter-04    ← RAG 파이프라인 구현
├── part03/chapter-05    ← RAG 성능 평가
│
├── part04/chapter-01    ← Function Calling + WeatherTool
├── part04/chapter-02    ← 도구 등록 + DatabaseTool
├── part04/chapter-03    ← 에이전트 설계 패턴
│
├── part05/chapter-01    ← 보안 (프롬프트 인젝션 방어)
├── part05/chapter-02    ← 테스트 전략
├── part05/chapter-03    ← Observability
├── part05/chapter-04    ← 비용 최적화 + 캐싱
│
├── part06/chapter-01    ← AI 챗봇 서비스
├── part06/chapter-02    ← 사내 문서 QA 시스템
└── part06/chapter-03    ← 이미지 생성 서비스
```

## 빠른 시작

### 사전 요구사항

| 도구 | 버전 | 설치 방법 |
|------|------|---------|
| JDK | 21 LTS | [Eclipse Temurin](https://adoptium.net/) |
| Gradle | 8.x (wrapper 포함) | `./gradlew` 사용 |
| Ollama | 최신 | [ollama.com](https://ollama.com) |

### 1. Ollama 설치 및 모델 다운로드

```bash
# macOS
brew install ollama
ollama pull llama3.2
ollama serve
```

### 2. 저장소 클론

```bash
git clone https://github.com/sung2ne/textbook-spring-ai.git
cd textbook-spring-ai
```

### 3. 챕터별 체크아웃

```bash
# PART 01 Ch 04 기준으로 실습
git checkout part01/chapter-04

# 서버 실행 (Ollama 로컬 — API 키 불필요)
./gradlew bootRun
```

### 4. 동작 확인

```bash
# 기본 채팅
curl "http://localhost:8080/api/chat?message=안녕하세요"

# 스트리밍 응답 (PART 02 Ch 03 이후)
curl "http://localhost:8080/api/chat/stream?message=Spring AI란?"
```

## OpenAI 연동 (선택)

```bash
export OPENAI_API_KEY=sk-proj-...
./gradlew bootRun
```

> 💡 개발 및 학습 목적으로는 Ollama 로컬 모델 사용을 권장합니다.

## 프로젝트 구조

```
src/main/java/com/example/springai/
├── SpringAiApplication.java
├── config/
│   └── AiConfig.java           — ChatClient, ChatMemory 빈 설정
├── controller/
│   ├── ChatController.java     — 기본 채팅 + SSE
│   └── RagController.java      — RAG API (PART 03~)
├── service/
│   ├── ChatService.java        — LLM 호출 핵심 로직
│   └── RagService.java         — RAG 파이프라인 (PART 03~)
├── tool/
│   ├── WeatherTool.java        — 날씨 조회 도구 (PART 04~)
│   └── DatabaseTool.java       — DB 조회 도구 (PART 04~)
└── domain/

src/main/resources/
├── application.yml             — 기본 설정
├── application-dev.yml         — 개발 환경 (Ollama)
└── prompts/                    — 프롬프트 템플릿 파일
```

## 라이선스

MIT License
