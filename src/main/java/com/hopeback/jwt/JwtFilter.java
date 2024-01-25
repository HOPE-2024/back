package com.hopeback.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter { // OncePerRequestFilter : 모든 HTTP 요청에 대해 한 번만 실행되는 필터틀 의미
    // OncePerRequestFilter를 상속받아 JWT 토큰을 필터링 하는 메서드
    public static final String AUTHORIZATION_HEADER = "Authorization";  // JWT 토큰을 전달 받는 HTTP Header 이름인 Authorization을 상수로 정의
    public static final String BEARER_PREFIX = "Bearer";  // Authorization Header 값 앞에 오는 접두사 Bearer를 상수로 의미. 실제 토큰을 분리하기 위해 사용됨.
    private final TokenProvider tokenProvider;   // 토큰 생성 & 검증을 수행하는 TokenProvider

    //  HTTP 요청이 들어오면 Authorization 헤더에서 JWT 토큰을 가져오는 메소드
    private String resolveToken(HttpServletRequest request) {       // 토큰을 요청 헤더에서 꺼내오는 메서드
    String bearerToken = request.getHeader(AUTHORIZATION_HEADER);   // 헤더에서 토큰 꺼냄
        if (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)) {  // 토큰이 있고 Bearer 접두사가 있다면
            return bearerToken.substring(7);  // 접두사를 제거한 토큰 문자열을 반환. 첫번째 글자는 0, 7부터 끝까지 선택
        }
        return bearerToken;
    }

    // jwt 토큰 검증 및 인증 처리 수행하는 메서드. 실제 필터 작업을 수행함
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String jwt = resolveToken(request);  // HTTP 요청에서 토큰을 추출
        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {  // 토큰 있고 유효한 토큰인지 검증
            Authentication authentication = tokenProvider.getAuthentication(jwt);  // 검증된 토큰으로 인증 객체 생성
            SecurityContextHolder.getContext().setAuthentication(authentication);  // 인증 객체를 SecurityContext에 저장
        }

        filterChain.doFilter(request, response);   // 다음 필터 체인으로 요청/응답 전달
    }

}
