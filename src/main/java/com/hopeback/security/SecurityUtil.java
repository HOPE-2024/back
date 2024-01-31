package com.hopeback.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class SecurityUtil {
    // Jackson 라이브러리의 ObjectMapper를 사용하여 JSON과 Java 객체 간의 변환을 수행하는 데 사용
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // 보안 관련 작업을 돕는 메소드 : Spring Security를 사용하여 현재 사용자의 회원 ID를 가져오는 유틸리티 클래스
    private SecurityUtil() {
        // private 생성자는 SecurityUtil 클래스의 인스턴스화를 방지하기 위해 사용. 객체 생성 방지 목적
        // 즉, 이 클래스는 상태를 가지지 않고(static = 정적 메소드만을 가진 클래스) 유틸리티 메소드만을 제공.
    }

    // Security Context의 Authentication 객체를 이용해 인증에 성공한 회원의 정보를 가져오는 메소드
    public static String getCurrentMemberId() {
        // Authentication 객체는 현재 인증된 사용자의 정보를 담고 있음
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            String authenticationJson = objectMapper.writeValueAsString(authentication);
            log.warn("Authentication 정보: " + authenticationJson);
        } catch (Exception e) {
            log.error("Authentication 정보를 JSON으로 변환하는 중 오류 발생: " + e.getMessage());
        }
        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("Security Context에 인증 정보가 없습니다.");
        }
        return authentication.getName();
    }
}
