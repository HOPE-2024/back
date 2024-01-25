package com.hopeback.service;

import com.hopeback.dto.member.MemberResDto;
import com.hopeback.entity.member.Member;
import com.hopeback.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    //모든 정지회원 조회
    public List<MemberResDto> stopChattingList() {
        List<Member> members = memberRepository.findByActive(0);
        // Member 엔티티를 MemberResDto로 매핑하여 리스트로 반환
        return members.stream()
                .map(member -> modelMapper.map(member, MemberResDto.class))
                .collect(Collectors.toList());
    }
    //모든 정지회원 조회
    public List<MemberResDto> stopMemberList() {
        List<Member> members = memberRepository.findByActive(1);
        // Member 엔티티를 MemberResDto로 매핑하여 리스트로 반환
        return members.stream()
                .map(member -> modelMapper.map(member, MemberResDto.class))
                .collect(Collectors.toList());
    }



}
