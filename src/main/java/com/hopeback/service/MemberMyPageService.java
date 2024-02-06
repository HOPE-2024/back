package com.hopeback.service;

import com.hopeback.dto.member.MemberMyPageDto;
import com.hopeback.entity.member.Member;
import com.hopeback.repository.MemberRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
@Getter
@Setter
@ToString
public class MemberMyPageService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원 상세조회
    public MemberMyPageDto getMemberDetail(String memberId) {
        log.info("memberService 회원상세조회 멤버아이디 : {}", memberId);
        Member member = memberRepository.findByMemberId(memberId).orElseThrow(
                () -> new RuntimeException("해당회원이 존재하지 않습니다.")
        );
        log.info("회원 상세조회 member정보 가져오기 : 닉네임은 {}", member.getNickName());
        return convertEntityDto(member);
    }

    // 회원번호 수정
    public boolean modifyMember(MemberMyPageDto memberMyPageDto) {
        try {
            Member member = memberRepository.findByEmail(memberMyPageDto.getEmail()).orElseThrow(
                    () -> new RuntimeException("회원수정 : 해당 회원이 존재하지 않습니다.")
            );
            if (memberMyPageDto.getPassword() != null) {
                member.setPassword(memberMyPageDto.getPassword());
                member.passwordEncode(passwordEncoder);
            } else {
                member.setProfile(memberMyPageDto.getProfile().isEmpty() ? member.getProfile() : memberMyPageDto.getProfile());
                member.setNickName(memberMyPageDto.getNickName().isEmpty() ? member.getNickName() : memberMyPageDto.getNickName());
            }
            memberRepository.save(member);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    // 회원 엔티티를 회원 DTO로 변환
    private MemberMyPageDto convertEntityDto(Member member) {
        MemberMyPageDto memberMyPageDto = new MemberMyPageDto();
        memberMyPageDto.setId(member.getId());
        memberMyPageDto.setMemberId(member.getMemberId());
        memberMyPageDto.setPassword(member.getPassword());
        memberMyPageDto.setName(member.getName());
        memberMyPageDto.setEmail(member.getEmail());
        memberMyPageDto.setProfile(member.getProfile());
        memberMyPageDto.setNickName(member.getNickName());
        memberMyPageDto.setPhone(member.getPhone());
        memberMyPageDto.setActive(member.getActive());
        return memberMyPageDto;
    }
}
