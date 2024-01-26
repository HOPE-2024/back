package com.hopeback.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
// 사용자가 로그인 하지 않았거나, 인증에 실패 했을 때 '401 unauthorized' 상태 코드를 클라이언트에게 보내는 메소드
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // 인증에 실패하면 401 Unauthorized 에러를 리턴
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }
}