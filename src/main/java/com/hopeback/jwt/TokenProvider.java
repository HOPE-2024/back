package com.hopeback.jwt;

import com.hopeback.dto.jwt.TokenDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

// JWT 토큰을 생성 및 검증하며, 토큰에서 회원 정보를 추출하는 클래스
@Slf4j
@Component
public class TokenProvider {
    // JWT를 생성하고 검증하는 기능을 제공하는 클래스
    private static final String AUTHORITIES_KEY = "auth"; // 토큰에 저장되는 권한 정보의 key
    private static final String BEARER_TYPE = "Bearer"; // 토큰 타입
    private static final long ACCESS_TOKEN_EXPIRE_TIME =  10000;  //1000 * 60 * 60; // 1시간 (3600000)
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7L;  // 7일 (604800000)
    private final Key key; // 토큰 서명을 하기 위한 key

    // springframework.beans.factory.annotation.Value
    // @Value : 설정 파일에서 JWT를 만들 때 사용할 암호화 키를 가져오기 위한 어노테이션
    public TokenProvider(@Value("${jwt.secret}") String secretKey) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes()); // HS256 알고리즘으로 새로운 키를 생성
    }

    // 토큰 생성
    // 인증에 성공한 사용자의 인증 정보를 매개 변수로 받음.
    public TokenDto generateTokenDto(Authentication authentication) {
        // 권한 정보 문자열로 변환 후 토큰 생성
        String authorities = authentication.getAuthorities().stream()
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
                .claim(AUTHORITIES_KEY, authorities)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        // 리프레시 토큰 생성, 액세스 토큰과 동일한 방식으로 생성
        String refreshToken = io.jsonwebtoken.Jwts.builder()
                .setSubject(authentication.getName())  // 사용자의 이름을 토큰의 주제로 설정
                .claim(AUTHORITIES_KEY, authorities)   // 권한 정보를 클레임으로 추가
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
    private Claims parseClaims(String token) {
        try {
            // 토큰의 헤더에 일반적으로 어떠한 알고리즘으로 암호화 되었는지가 기술되어있다. ( "alg" : "HS512" )
            // 액세스 토큰 파싱, 토큰을 파싱하여 클레임을 가져옴. 서명 검증에 사용되는 키 설정
            return io.jsonwebtoken.Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            // ExpiredJwtException : 토큰이 만료 되었다. -> 토큰이 만료되면 ExpiredJwtException 발생하고 만료된 토큰데 담긴 클레임을 반환
            return e.getClaims();
        }
    }

    // 로그인 함수에서 사용자가 입력한 정보를 토대로 토큰을 생성하고, 해당 토큰을 이용하여 인증을 시도해 성공 시 새로운 토큰을 생성했었다.
    // 고로 한 번의 복호화를 거치면 "사용자가 입력한 정보를 토대로 토큰을 생성" 의 토큰이 반환된다.
    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token); // 클레임 : 페이로드에 저장된 정보들을 의미

        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 권한 정보 추출
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        log.warn("클레임 : " + claims.getSubject());
        // 두번째 매개변수는 사용자의 자격 증명을 나타냄. 비빌먼호는 이는 인증이 이미 완료되었으며, 비밀번호 정보가 더 이상 필요하지 않음
        UserDetails principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    // 토큰의 유효성 검증
    public boolean validateToken(String token) {
        try { // 토큰을 파싱하여 유효하면 -> 토큰 파서 빌더 생성 후 서명키 설정, 토큰 파싱
            io.jsonwebtoken.Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.warn("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.warn("만료된 JWT 토큰입니다."); // 이때, 401 에러를 반환
        } catch (UnsupportedJwtException e) {
            log.warn("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.warn("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    // access 토큰 재발급
    public String generateAccessToken(Authentication authentication) {
        // generateTokenDto 메서드를 사용하여 액세스 토큰을 생성하고 그 결과로 얻은 tokenDto 객체에서 액세스 토큰을 가져와서 반환.
        return generateTokenDto(authentication).getAccessToken();
    }
}