package com.hopeback.service.jwt;

import com.hopeback.dto.member.MemberReqDto;
import com.hopeback.dto.member.MemberResDto;
import com.hopeback.entity.member.Member;
import com.hopeback.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    // 로그인

}
