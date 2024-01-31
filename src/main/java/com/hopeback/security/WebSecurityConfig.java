package com.hopeback.security;


import com.hopeback.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebSecurity  // Spring Security를 활성화
@RequiredArgsConstructor
@Configuration  // 스프링 구성 클래스임을 나타냄
@Component
// Spring Security 설정과 관련된 설정 클래스
public class WebSecurityConfig implements WebMvcConfigurer {

    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint; // 인증 실패 시 처리할 클래스 401
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler; // 인가 실패시 처리할 클래스 403

    // 비밀번호 암호화를 위한 PasswordEncoder Bean을 생성하는 메서드
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // BCrypt 알고리즘으로 암호화
    }

    // HTTP 보안 설정을 정의
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic() // HTTP 기본 인증 활성화
                .and()
                .csrf().disable() // 개발 환경에서의 편의를 위해 CSRF 보안 기능 비활성화
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 서버에서 세션 상태를 유지하지 않는다는 의미
                .and()
                .exceptionHandling() // 인증 및 인가 예외 처리를 정의
                .authenticationEntryPoint(jwtAuthenticationEntryPoint) // 인증 실패시 처리할 클래스 지정
                .accessDeniedHandler(jwtAccessDeniedHandler) // 인가 실패 시 처리할 클래스 지정
                .and()
                .authorizeRequests()  // URL 패턴에 따른 접근 권한을 설정

                // 특정 경로에 대해서 인증 없이 허용
                .antMatchers("/auth/**", "/ws/**", "/test/**", "/admin/**", "/chat/**", "/elastic/**", "/refresh/**").permitAll()

                // Swagger 에 관련된 리소스에 대해서 인증 없이 허용
                .antMatchers("/v2/api-docs", "/swagger-resources/**", "/swagger-ui.html", "/webjars/**", "/swagger/**", "/sign-api/exception").permitAll()
                .anyRequest().authenticated() // 나머지 모든 요청은 인증이 필요
                .and()

                // .apply 메서드 : HttpSecurity 객체에 구성을 적용하기 위한 메서드이다.
                // 즉 사용자가 정의한 JwtSecurityConfig 클래스를 HttpSecurity에 적용하는 것
                .apply(new JwtSecurityConfig(tokenProvider))
                .and()
                .cors(); // CORS 설정 추가

        return http.build();
    }

    // CORS 설정
    @Override
    public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**")
                    .allowedOrigins("http://localhost:3000") // 특정 origin인 localhost:3000에 대해서 허용
                    .allowedMethods("*")   // 모든 HTTP 메소드 허용
                    .allowedHeaders("*")   // 모든 header 허용
                    .allowCredentials(false);  // 인증이 필요한 요청에 대해서 크리덴셜 전송 허용, 일반적으로 쿠키를 사용할 때 사용
    }
}

