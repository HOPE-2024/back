package com.hopeback.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hopeback.dto.member.MemberResDto;
import com.hopeback.entity.member.Member;
import com.hopeback.jwt.TokenProvider;
import com.hopeback.repository.MemberRepository;
import com.hopeback.repository.jwt.RefreshTokenRepository;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
