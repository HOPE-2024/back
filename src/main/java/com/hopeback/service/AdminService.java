package com.hopeback.service;

import com.hopeback.dto.member.MemberResDto;
import com.hopeback.entity.member.Member;
import com.hopeback.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AdminService {
    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;

    //모든 회원 조회
    public List<MemberResDto> selectMemberList() {
        List<Member> members = memberRepository.findAll();

        // Member 엔티티를 MemberResDto로 매핑하여 리스트로 반환
        return members.stream()
                .map(member -> modelMapper.map(member, MemberResDto.class))
                .collect(Collectors.toList());
    }

    //모든 정지 회원 조회
    public List<MemberResDto> stopChattingList() {
        List<String> activeList = List.of("7일 정지", "30일 정지");
        List<Member> members = memberRepository.findByActiveIn(activeList);
        // Member 엔티티를 MemberResDto로 매핑하여 리스트로 반환
        return members.stream()
                .map(member -> modelMapper.map(member, MemberResDto.class))
                .collect(Collectors.toList());
    }

    //정지 회원 조회
    public List<MemberResDto> stopMemberList() {
        List<Member> members = memberRepository.findByActive("회원 정지");
        // Member 엔티티를 MemberResDto로 매핑하여 리스트로 반환
        return members.stream()
                .map(member -> modelMapper.map(member, MemberResDto.class))
                .collect(Collectors.toList());
    }

    //이름으로 회원 조회
    public List<MemberResDto> selectMember(String name) {
        List<Member> members = memberRepository.findByNameContaining(name);
        // Member 엔티티를 MemberResDto로 매핑하여 리스트로 반환
        return members.stream()
                .map(member -> modelMapper.map(member, MemberResDto.class))
                .collect(Collectors.toList());
    }

    @Transactional
    //회원 정지
    public Boolean updateActive(MemberResDto memberResDto) {
        Member member = memberRepository.findById(memberResDto.getId()).orElseThrow(
                () -> new RuntimeException("해당 회원이 존재하지 않습니다.")
        );
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime sevenDaysLater =null;
        if(memberResDto.getActive() == "7일 정지"){
            sevenDaysLater = now.plus(7, ChronoUnit.DAYS);
        }
        if(memberResDto.getActive() == "30일 정지"){
            sevenDaysLater = now.plus(30, ChronoUnit.DAYS);
        }

        // Member 엔티티를 MemberResDto로 매핑하여 리스트로 반환
        if(member != null){
            member.setActive(memberResDto.getActive());
            member.setActive_date(sevenDaysLater);
            return true;
        }else {
            return  false;
        }
    }
}