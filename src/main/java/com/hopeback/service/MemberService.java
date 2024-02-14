package com.hopeback.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hopeback.dto.jwt.TokenDto;
import com.hopeback.dto.member.MemberDto;
import com.hopeback.dto.member.MemberResDto;
import com.hopeback.entity.jwt.RefreshToken;
import com.hopeback.entity.member.Member;
import com.hopeback.jwt.TokenProvider;
import com.hopeback.repository.MemberRepository;
import com.hopeback.repository.jwt.RefreshTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Slf4j
@Service
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final ObjectMapper objectMapper;

    @Autowired   // Autowired : 필드, 생성자, 세터 메서드에 해당하는 타입의 빈을 찾아 자동으로 주입
    public MemberService(MemberRepository memberRepository, TokenProvider tokenProvider, RefreshTokenRepository refreshTokenRepository, ObjectMapper objectMapper) {
        this.memberRepository = memberRepository;
        this.tokenProvider = tokenProvider;
        this.refreshTokenRepository = refreshTokenRepository;
        this.objectMapper = objectMapper;
    }

    // 회원 상세 조회
    public MemberResDto getMemberDetail(String memberId) {
        Member member = memberRepository.findByMemberId(memberId).orElseThrow(
                ()-> new RuntimeException("해당 회원이 존재하지 않습니다."));
        return convertEntityDto(member);
    }

    // 카카오
    // 카카오 회원 가입 여부
    public boolean kakaoSignupCheck(String kakaoNickName) {
        return memberRepository.existsByNickName(kakaoNickName);
    }

    // 카카오 회원 가입
    public boolean kakaoSignup(MemberDto memberDto) {
        try {
            String uniqueEmail = UUID.randomUUID().toString() + "@kakao.com";  // UUID : 전역적으로 고유한 값을 생성하기 위해서 사용. 중복을 피하고 고유성을 보장
        Member member = Member.builder()
                .nickName(memberDto.getNickName())
                .name("카카오")
                .email(uniqueEmail)
                .password("kakaoPassword")
                .phone("010-0000-0000")
                .authority(memberDto.getAuthority())
                .image(memberDto.getImage())
                .build();
        memberRepository.save(member);
        return true;
        } catch (Exception e) {
            log.warn("카카오 회원가입 오류 발생 !! : ", e);
            return false;
        }
    }

    // 카카오 서버로부터 사용자 정보를 조회
    // 카카오 서버에 HTTP GET 요청을 보내서 사용자 정보를 요청하고, 그에 대한 응답을 문자열 형태로 받아옵니다. 받아온 정보는 후속 처리를 위해 반환
    public String requestKakaoUserInfo(String kakaoToken) {
        // 사용자 정보를 조회하기 위한 지정 url
        final String requestUrl = "https://kapi.kakao.com/v2/user/me";

        // http 요청 헤더를 생성하여 카카오 액세스 토큰을 설정
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer" + kakaoToken);

        // RestTemplate : 외부 API와 통실할 때 사용. RESTful 서비스와 상호 작용하거나, 외부 서비스와 데이터를 교환할 때 사용하여 HTTP 통신 처리.
        RestTemplate restTemplate = new RestTemplate();

        // 카카오 api에 get 요청을 보내고 응답을 ResponseEntity<String> 형태로 받음.
        // String.class : 보통 RESTful API를 호출할 때 응답은 JSON 형식으로 제공. 이러한 경우 해당 객체를 이용하여 응답을 문자열로 변환함.
        ResponseEntity<String> responseEntity = restTemplate.exchange(requestUrl, HttpMethod.GET, new HttpEntity<>(headers), String.class);

        log.warn("카카오 API 응답: " + responseEntity.getBody());
        return responseEntity.getBody();
    }

    // 카카오 로그인.
    public TokenDto kakaoLogin(String kakaoNickName) throws JsonProcessingException {
        Member member = memberRepository.findByNickName(kakaoNickName).orElseThrow(
                () -> new RuntimeException("해당 닉네임으로 조회되는 카카오 회원이 없습니다.")
        );

        // 카카오 회원 정보를 인증 객체로 변환
        String memberId = member.getId().toString();
        Authentication authentication = new UsernamePasswordAuthenticationToken(memberId,"", member.getAuthority());

        // 액세스 및 리프레스 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);
        // 해당 회원에 대한 리프레스 토큰 조회
        RefreshToken retrievedRefreshToken = refreshTokenRepository
                .findByMember(member)
                .orElse(null);
        // 리프레시 토큰이 없으면 새로 생성하여 저장
        if(retrievedRefreshToken == null) {
            RefreshToken newRefreshToken = RefreshToken.builder()
                    .refreshToken(tokenDto.getRefreshToken())
                    .refreshTokenExpiresIn(tokenDto.getRefreshTokenExpiresIn())
                    .member(member)
                    .build();
            refreshTokenRepository.save(newRefreshToken);
        }else {  // 이미 리프레시 토큰이 있으면 업데이트
            log.warn("kakaoLogin시 해당 회원 앞으로 받은 리프레시 토큰 확인 : {}", retrievedRefreshToken.getRefreshToken());
            retrievedRefreshToken.update(tokenDto.getRefreshToken(), tokenDto.getRefreshTokenExpiresIn());
        }
        log.warn("MemberService kakaoLogin 반환되는 토큰은 : " + objectMapper.writeValueAsString(tokenDto));
        return tokenDto;
    }






    // 회원 엔티티를 회원 DTO로 변환
    private MemberResDto convertEntityDto(Member member) {
        MemberResDto memberDto = new MemberResDto();
        memberDto.setId(member.getId());
        memberDto.setMemberId(member.getMemberId());
        memberDto.setPassword(member.getPassword());
        memberDto.setName(member.getName());
        memberDto.setEmail(member.getEmail());
        memberDto.setNickName(member.getNickName());
        memberDto.setPhone(member.getPhone());
        memberDto.setActive(member.getActive());
        return memberDto;
    }

}
