package com.hopeback.jwt;

import com.hopeback.dto.jwt.TokenDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TokenProvider {
    // JWT를 생성하고 검증하는 기능을 제공하는 클래스
    private static final String AUTHORITIES_KEY = "auth"; // 토큰에 저장되는 권한 정보의 key
    private static final String BEARER_TYPE = "Bearer"; // 토큰 타입
    private static final long ACCESS_TOKEN_EXPIRE_TIME =  60 * 60 * 1000;  //1000 * 60 * 60; // 1시간 (3600000)
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7L;  // 7일 (604800000)
    private final Key key; // 토큰 서명을 하기 위한 key

    // @Value : JWT를 만들 때 사용하는 암호화 키 값을 생성
    public TokenProvider(@Value("${jwt.secret}") String secretKey) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes()); // HS256 알고리즘으로 새로운 키를 생성
    }


    // 토큰 생성
    // 인증에 성공한 사용자의 인증 정보를 매개 변수로 받음.
    public TokenDto generateTokenDto(Authentication authentication) {
        // 권한 정보 문자열로 변환 후 토큰 생성
        String authotities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        // 토큰 만료 시간 설정
        long now = (new java.util.Date()).getTime(); // 현재 시간
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);  // 액세스 토큰 만료 시간
        Date refreshTokenExpiresIn = new Date(now + REFRESH_TOKEN_EXPIRE_TIME); // 리프레시 토큰 만료 시간

        log.info("TP getName {}",authentication.getName());    // 사용자의 이름
        log.info("TP getPrincipal {}",authentication.getDetails());  //  사용자의 아이디, 비밀번호, 권한 등의 정보를 포함
        log.info("TP getAuthorities {}",authentication.getAuthorities());  // 사용자가 가지고 있는 권한

        // 토큰 생성
        String accessToken = io.jsonwebtoken.Jwts.builder() // Jwts.builder()를 사용하여 빌더를 시작
                .setSubject(authentication.getName())  // 사용자의 이름을 토큰의 주제로 설정
                .claim(AUTHORITIES_KEY, authotities)   // 권한 정보를 클레임으로 추가
                .setExpiration(accessTokenExpiresIn)   // 만료 시간과 서명 알고리즘을 설정한 후 키를 사용하여 서명
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        // 리프레시 토큰 생성, 액세스 토큰과 동일한 방식으로 생성
        String refreshToken = io.jsonwebtoken.Jwts.builder()
                .setSubject(authentication.getName())  // 사용자의 이름을 토큰의 주제로 설정
                .claim(AUTHORITIES_KEY, authotities)   // 권한 정보를 클레임으로 추가
                .setExpiration(refreshTokenExpiresIn)   // 만료 시간과 서명 알고리즘을 설정한 후 키를 사용하여 서명
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        log.info("TOKENPRO RFTK 토큰 생성 !!: {}", refreshToken);

        // 토큰 정보를 담은 TokenDto 객체 생성
        return TokenDto.builder()
                .grantType(BEARER_TYPE)    // 토큰의 타입을 Bearer로 설정
                .accessToken(accessToken)  // 액세스 토큰 설정
                .accessTokenExpiresIn(accessTokenExpiresIn.getTime())  // 액세스 토큰의 만료 시간을 milliseconds로 설정.
                .refreshToken(refreshToken)  // 리프레시 토큰 설정
                .refreshTokenExpiresIn(refreshTokenExpiresIn.getTime())  // 리프레시 토큰의 만료 시간을 milliseconds로 설정.
                .build();
    }

    // 토큰 복호화 : 저장된 정보를 추출하는 과정으로 헤더+페이로드(내용)+서명으로 구성.
    // 복호화는 이중 페이로드인 Claims 정보를 추출하는 작업.
    public Claims parseClaims(String accessToken) {
        try {
            // 액세스 토큰 파싱, 토큰을 파싱하여 클레임을 가져옴. 서명 검증에 사용되는 키 설정
            return io.jsonwebtoken.Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            // ExpiredJwtException : 토큰이 만료 되었다. -> 토큰이 만료되면 ExpiredJwtException 발생하고 만료된 토큰데 담긴 클레임을 반환
            return e.getClaims();
        }
    }

    // 토큰에서 회원 정보 추출
    public  Authentication getAuthentication(String accessToken) {
        // 토큰 복호화
        Claims claims = parseClaims(accessToken);
        // 토큰 복호화에 실패하면
        if (claims.get(AUTHORITIES_KEY) == null) {  // 권한키 확인
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 토큰에 담긴 권한 정보 가져옴
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))  // 권한 정보를 쉼표로 문자열 분리
                        .map(SimpleGrantedAuthority::new) // SimpleGrantedAuthority 객체로 매핑
                        .collect(Collectors.toList());    // 권한 목록 생성

        User principal = new User(claims.getSubject(), "", authorities);
        // 인증된 사용자를 나타냄
        return new UsernamePasswordAuthenticationToken(principal, accessToken, authorities);
    }

    // 토큰의 유효성 검증
    public boolean validateToken(String token) {
        try { // 토큰을 파싱하여 유효하면 -> 토큰 파서 빌더 생성 후 서명키 설정, 토큰 파싱
            io.jsonwebtoken.Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            log.info("서명키 뭐냐 !!: {}", key);
            if(key == null) {
                log.error("key 값이 null입니다");
            }
            return true;
        } catch (SecurityException | io.jsonwebtoken.MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않은 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    // access 토큰 재발급
    public String generateAccessToken(Authentication authentication) {
        // generateTokenDto 메서드를 사용하여 액세스 토큰을 생성하고 그 결과로 얻은 tokenDto 객체에서 액세스 토큰을 가져와서 반환.
        return generateTokenDto(authentication).getAccessToken();
    }

}
