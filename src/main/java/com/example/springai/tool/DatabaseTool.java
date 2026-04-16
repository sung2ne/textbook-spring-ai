package com.example.springai.tool;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * PART 04 Ch 02: 도구 등록과 실행 — DatabaseTool
 */
@Component
public class DatabaseTool {

    @Tool(description = "상품 ID로 상품 정보를 조회합니다")
    public ProductInfo getProduct(@ToolParam(description = "조회할 상품 ID") Long productId) {
        // 더미 데이터 (실습용)
        return new ProductInfo(productId, "상품 " + productId, 10000, 50);
    }

    @Tool(description = "키워드로 상품을 검색합니다. 최대 3개까지 반환합니다.")
    public List<ProductInfo> searchProducts(
            @ToolParam(description = "검색 키워드") String keyword) {
        return List.of(
                new ProductInfo(1L, keyword + " A", 15000, 30),
                new ProductInfo(2L, keyword + " B", 20000, 10)
        );
    }

    public record ProductInfo(Long id, String name, int price, int stock) {}
}
