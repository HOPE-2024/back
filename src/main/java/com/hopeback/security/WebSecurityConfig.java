package com.hopeback.security;


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

@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
@Component
// Spring Security 설정과 관련된 설정 클래스
public class WebSecurityConfig implements WebMvcConfigurer {

    // 비밀번호를 암호화
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
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 사용 X
                .and()
                .exceptionHandling()
//                .authenticationEntryPoint(jwtAuthenticationEntryPoint) // 인증 실패시 처리할 클래스 지정
//                .accessDeniedHandler(jwtAccessDeniedHandler) // 인가 실패 시 처리할 클래스 지정
                .and()
                .authorizeRequests()

                // 특정 경로에 대해서 인증 없이 허용
                .antMatchers("/auth/**", "/ws/**", "/movies/**", "/test/**", "/auction/new/**", "/auction/**", "/Review/**", "/member/**", "/email/**", "/MyPage/**",  "/refresh/**").permitAll()

                // Swagger 에 관련된 리소스에 대해서 인증 없이 허용
                .antMatchers("/v2/api-docs", "/swagger-resources/**", "/swagger-ui.html", "/webjars/**", "/swagger/**", "/sign-api/exception").permitAll()
                .anyRequest().authenticated() // 나머지 모든 요청은 인증이 필요
                .and()

                // .apply 메서드 : HttpSecurity 객체에 구성을 적용하기 위한 메서드이다.
                // 즉 사용자가 정의한 JwtSecurityConfig 클래스를 HttpSecurity에 적용하는 것
//                .apply(new com.team.creer_back.security.JwtSecurityConfig(tokenProvider))
//                .and()
                .cors(); // CORS 설정 추가

        return http.build();
    }

    // CORS 설정
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000") // 해당 도메인에서 오는 모든 요청을 허용
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}

