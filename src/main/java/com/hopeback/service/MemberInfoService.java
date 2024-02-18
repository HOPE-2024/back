package com.hopeback.service;

import com.hopeback.dto.member.MemberInfoDto;
import com.hopeback.entity.member.Member;
import com.hopeback.entity.member.MemberInfo;
import com.hopeback.repository.MemberInfoRepository;
import com.hopeback.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberInfoService {

    private final MemberInfoRepository memberInfoRepository;
    private final MemberRepository memberRepository;

    public MemberInfoDto getMemberInfo(String memberId) {
        MemberInfo memberInfo = memberInfoRepository.findByMember_MemberId(memberId)
                .orElseThrow(() -> new RuntimeException("Member info not found for memberId: " + memberId));
        return convertEntityToDto(memberInfo);
    }

    public boolean updateMemberInfo(String memberId, MemberInfoDto memberInfoDto) {
        try {
            Member member = memberRepository.findByMemberId(memberId)
                    .orElseGet(() -> {
                        Member newMember = new Member();
                        newMember.setMemberId(memberId);
                        return memberRepository.save(newMember);
                    });

            MemberInfo memberInfo = memberInfoRepository.findByMember_MemberId(memberId)
                    .orElseGet(() -> {
                        MemberInfo newMemberInfo = new MemberInfo();
                        newMemberInfo.setMember(member);
                        return newMemberInfo;
                    });

            log.info("멤버 데이터: {}", memberInfo);

            // Update member info fields
            memberInfo.setBirthDate(memberInfoDto.getBirthDate() != null ? memberInfoDto.getBirthDate() : memberInfo.getBirthDate());
            memberInfo.setHeight(memberInfoDto.getHeight() != null ? memberInfoDto.getHeight() : memberInfo.getHeight());
            memberInfo.setWeight(memberInfoDto.getWeight() != null ? memberInfoDto.getWeight() : memberInfo.getWeight());
            memberInfo.setBmi(memberInfoDto.getBmi() != null ? memberInfoDto.getBmi() : memberInfo.getBmi());

            // Save updated member info
            memberInfoRepository.save(memberInfo);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private MemberInfoDto convertEntityToDto(MemberInfo memberInfo) {
        MemberInfoDto memberInfoDto = new MemberInfoDto();
        memberInfoDto.setId(memberInfo.getId());
        memberInfoDto.setMemberId(memberInfo.getMember().getMemberId());
        memberInfoDto.setBirthDate(memberInfo.getBirthDate());
        memberInfoDto.setHeight(memberInfo.getHeight());
        memberInfoDto.setWeight(memberInfo.getWeight());
        memberInfoDto.setBmi(memberInfo.getBmi());
        return memberInfoDto;
    }
}
