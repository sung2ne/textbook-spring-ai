package com.example.springai.service;

import org.springframework.stereotype.Service;

/**
 * PART 06 Ch 02: 부서별 문서 접근 권한 제어
 */
@Service
public class AccessControlService {

    public boolean canAccess(String userId, String department) {
        // 실습용 간단 구현: "admin"은 모든 부서 접근 가능
        if ("admin".equals(userId)) return true;
        // 실제 구현에서는 DB에서 사용자-부서 매핑 조회
        return getUserDepartment(userId).equals(department);
    }

    private String getUserDepartment(String userId) {
        // 더미 데이터 (실습용)
        return switch (userId) {
            case "user1" -> "engineering";
            case "user2" -> "hr";
            default -> "public";
        };
    }
}
