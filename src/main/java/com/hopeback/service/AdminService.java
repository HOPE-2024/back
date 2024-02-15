package com.hopeback.service;

import com.hopeback.constant.Authority;
import com.hopeback.dto.admin.QueryDto;
import com.hopeback.dto.admin.ReplyDto;
import com.hopeback.dto.admin.ReportDto;
import com.hopeback.dto.member.MemberDto;
import com.hopeback.dto.member.MemberResDto;
import com.hopeback.entity.admin.Query;
import com.hopeback.entity.admin.Reply;
import com.hopeback.entity.admin.Report;
import com.hopeback.entity.member.Member;
import com.hopeback.repository.MemberRepository;
import com.hopeback.repository.QueryRepository;
import com.hopeback.repository.ReplyRepository;
import com.hopeback.repository.ReportRepository;
import com.hopeback.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {
    private final MemberRepository memberRepository;
    private final ReportRepository reportRepository;
    private final QueryRepository queryRepository;
    private final ReplyRepository replyRepository;
    private final ModelMapper modelMapper;

    //모든 회원 조회
    public List<MemberDto> selectMemberList() {
        List<Member> members = memberRepository.findAll();

        // Member 엔티티를 MemberResDto로 매핑하여 리스트로 반환
        return members.stream()
                .map(member -> modelMapper.map(member, MemberDto.class))
                .collect(Collectors.toList());
    }

    //모든 정지 회원 조회
    @Transactional
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
    public List<MemberResDto> selectMemberName(String name) {
        List<Member> members = memberRepository.findByNameContaining(name);
        // Member 엔티티를 MemberResDto로 매핑하여 리스트로 반환
        return members.stream()
                .map(member -> modelMapper.map(member, MemberResDto.class))
                .collect(Collectors.toList());
    }
    //id로 회원 조회
    public List<MemberResDto> selectMemberId(String name) {
        List<Member> members = memberRepository.findByMemberIdContaining(name);
        // Member 엔티티를 MemberResDto로 매핑하여 리스트로 반환
        return members.stream()
                .map(member -> modelMapper.map(member, MemberResDto.class))
                .collect(Collectors.toList());
    }
    //닉네임으로 회원 조회
    public List<MemberResDto> selectMemberNick(String name) {
        List<Member> members = memberRepository.findByNickNameContaining(name);
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
        LocalDateTime sevenDaysLater = null;
        if (memberResDto.getActive().equals("7일 정지")) {
            sevenDaysLater = now.plus(7, ChronoUnit.DAYS);
        }
        if (memberResDto.getActive().equals("30일 정지")) {
            sevenDaysLater = now.plus(30, ChronoUnit.DAYS);
        }
        if (sevenDaysLater != null && sevenDaysLater.isBefore(now)) {
            throw new IllegalArgumentException("7일 뒤의 날짜는 현재 날짜보다 이전일 수 없습니다.");
        }
        // Member 엔티티를 MemberResDto로 매핑하여 리스트로 반환
        if (member != null) {
            member.setActive(memberResDto.getActive());
            member.setActive_date(sevenDaysLater);
            return true;
        } else {
            return false;
        }
    }


    // 페이지네이션으로  모든 데이터 출력
    public List<MemberDto> selectMemberPageList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Member> members = memberRepository.findAll(pageable).getContent();
        return members.stream()
                .map(member -> {
                    MemberDto memberDto = modelMapper.map(member, MemberDto.class);
                    return memberDto;
                })
                .collect(Collectors.toList());

    }


    // 페이지 수 조회
    public int memberPage(Pageable pageable) {
        return memberRepository.findAll(pageable).getTotalPages();
    }

    public List<MemberResDto> chatingMembers(int page, int size) {
        List<String> activeList = new ArrayList<>();
        activeList.add("7일 정지");
        activeList.add("30일 정지");
        Pageable pageable = PageRequest.of(page, size);
        List<Member> members = memberRepository.findByActiveIn(activeList, pageable);

        return members.stream()
                .map(member -> {
                    MemberResDto memberResDto = modelMapper.map(member, MemberResDto.class);
                    return memberResDto;
                })
                .collect(Collectors.toList());
    }

    // 페이지 수 조회
    public int chatingMembersPage(Pageable pageable) {
        int count = memberRepository.countByActiveIn(List.of("7일 정지", "30일 정지"));
        return (int) Math.ceil((double) count / pageable.getPageSize());
    }




    public List<MemberResDto> stopMember(int page, int size) {
        List<String> activeList = new ArrayList<>();
        activeList.add("회원 정지");


        Pageable pageable = PageRequest.of(page, size);
        List<Member> members = memberRepository.findByActiveIn(activeList, pageable);
        return members.stream()
                .map(member -> {
                    MemberResDto memberResDto = modelMapper.map(member, MemberResDto.class);
                    return memberResDto;
                })
                .collect(Collectors.toList());
    }

    // 페이지 수 조회
    public int stopMemberPage(Pageable pageable) {
        int count = memberRepository.countByActiveIn(List.of("회원 정지"));
        return (int) Math.ceil((double) count / pageable.getPageSize());
    }



    //내 문의 글 조회
    public List<QueryDto> oftenQuery() {
        // "읽기 전" 상태에 있는 문의 글 조회
        List<Query> reports = queryRepository.findByOftenContaining("FAQ");
        // 조회된 각 문의 글을 QueryDto 객체로 매핑하여 리스트로 반환
        return reports.stream()
                .map(query -> modelMapper.map(query, QueryDto.class))
                .collect(Collectors.toList());
    }




    @Transactional
//회원  되돌리기
    public Boolean chattingTime( ) {
        String memberId = SecurityUtil.getCurrentMemberId();
        Member member = memberRepository.findByMemberId(memberId).orElseThrow(
                () -> new RuntimeException("해당 회원이 존재하지 않습니다."));
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime sevenDaysLater = member.getActive_date();
        if ( sevenDaysLater.compareTo(now) > 0) {
            log.warn("정지 기간 남음");

            return true;
        } else {
            log.warn("정지 기간 지남");
            member.setActive("일반 유저");
            return false;
        }
    }


}