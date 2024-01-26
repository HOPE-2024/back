package com.hopeback.service.jwt;

import com.hopeback.dto.jwt.TokenDto;
import com.hopeback.dto.member.MemberReqDto;
import com.hopeback.dto.member.MemberResDto;
import com.hopeback.entity.member.Member;
import com.hopeback.jwt.TokenProvider;
import com.hopeback.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder managerBuilder;  // 인증 담당 클래스
    private final TokenProvider tokenProvider;

    // 중복 체크(id, nickName)
    public Boolean checkUnique(int type, String info) {
        boolean isUnique;
        switch (type) {
            case 0:
                isUnique = memberRepository.existsByMemberId(info);
                break;
            case 1:
                isUnique = memberRepository.existsByNickName(info);
                break;
            default: isUnique = true; log.info("중복 체크 잘못 됨!!");
        }
        return isUnique;
    }


    // 회원 가입
    public MemberResDto signup(MemberReqDto memberReqDto) {
        if (memberRepository.existsByMemberId(memberReqDto.getMemberId())) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }
        Member member = memberReqDto.toEntity(passwordEncoder);
        return MemberResDto.of(memberRepository.save(member));
    }

    // 로그인 (탈퇴 여부 확인 -> 기존 리프레시 토큰 보유시 삭제 후 재 생성)
//    public TokenDto login(MemberReqDto memberReqDto) {
//            try {
//        UsernamePasswordAuthenticationToken authenticationToken = memberReqDto.toAuthentication();  //requestDto 객체의 정보를 기반으로 AuthenticationToken 생성
//        log.info("authenticationToken : {}", authenticationToken);
//
//                // requestDto 객체의 정보를 기반으로 AuthenticationToken 생성, 인증 성공시 Authentication 객체 반환됨
//                Authentication authentication = managerBuilder.getObject().authenticate(authenticationToken);
//                log.info("authentication: {}", authentication);
//                // 인증 정보를 바탕으로 토큰 생성, TokenDto에 생성된 토큰 정보 들어있음
//                TokenDto token = tokenProvider.generateTokenDto(authentication);
//
//                // refreshToken DB에 저장
//                Member member = memberRepository.findByMemberId(memberReqDto.getMemberId())
//                        .orElseThrow(() -> new RuntimeException("존재하지 않은 아이디 입니다."));
//
//                // 탈퇴한 회원인지 체크
//
//
//    }


}
