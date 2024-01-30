package com.hopeback.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
// 보안 관련 작업을 돕는 메소드 : Spring Security를 사용하여 현재 사용자의 회원 ID를 가져오는 유틸리티 클래스
public class SecurityUtil {
    private SecurityUtil() {
        // private 생성자는 SecurityUtil 클래스의 인스턴스화를 방지하기 위해 사용. 객체 생성 방지 목적
        // 즉, 이 클래스는 상태를 가지지 않고(static = 정적 메소드만을 가진 클래스) 유틸리티 메소드만을 제공.
    }

    // Security Context의 Authentication 객체를 이용해 인증에 성공한 회원의 정보를 가져오는 메소드
    public static String getCurrentMemberId() {
        // Authentication 객체는 현재 인증된 사용자의 정보를 담고 있음
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {  // authentication 객체가 null이거나 authentication.getName()이 null일 경우,
            throw new RuntimeException("Security Context에 인증 정보가 없습니다.");  // 인증 정보가 없다는 RuntimeException 예외를 발생
        }
        return authentication.getName();  // authentication.getName()으로 사용자의 ID를 문자열 형태로 가져오고, Long 타입으로 파싱하여 반환
    }
}
