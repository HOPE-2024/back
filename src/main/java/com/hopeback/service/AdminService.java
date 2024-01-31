package com.hopeback.service;

import com.hopeback.dto.admin.ReportDto;
import com.hopeback.dto.member.MemberResDto;
import com.hopeback.entity.admin.Report;
import com.hopeback.entity.member.Member;
import com.hopeback.repository.MemberRepository;
import com.hopeback.repository.ReportRepository;
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
    private final ReportRepository reportRepository;
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
        LocalDateTime sevenDaysLater =null;
        if(memberResDto.getActive().equals("채팅 7일 정지")) {
            sevenDaysLater = now.plus(7, ChronoUnit.DAYS);
        }
        if(memberResDto.getActive().equals("채팅 30일 정지")) {
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



    //모든 신고 조회
    public List<ReportDto> selectReportList() {
        List<Report> reports = reportRepository.findAll();

        // Member 엔티티를 MemberResDto로 매핑하여 리스트로 반환
        return reports.stream()
                .map(report -> {
                    ReportDto reportDto = modelMapper.map(report, ReportDto.class);
                    reportDto.setReporter(report.getReporter());
                    reportDto.setReported(report.getReported());
                    return reportDto;
                })
                .collect(Collectors.toList());
    }

    //신고 내역 삭제
    public Boolean deleteReport(Long id){
        Report report = reportRepository.findById(id).orElseThrow(
                () -> new RuntimeException("신고 내역이 없습니다."));
        if(report !=null){
            reportRepository.deleteById(id);
            return true;
        }else {
            return false;
        }
    }
//신고 내용 읽음으로 변경

    public Boolean updateReportStatus(Long id){
        Report report = reportRepository.findById(id).orElseThrow(
                () -> new RuntimeException("신고 내역이 없습니다."));
        if(report !=null){
            report.setStatus("읽음");
            return true;
        }else {
            return false;
        }
    }
    public Boolean updateReportStatus(ReportDto reportDto){
        Report report = reportRepository.findById(reportDto.getId()).orElseThrow(
                () -> new RuntimeException("신고 내역이 없습니다."));
        if(report !=null){
            report.setStatus(reportDto.getStatus());
            return true;
        }else {
            return false;
        }
    }

    // 처리 전 신고 목록 조회
    public List<ReportDto> selectBeforeReport() {
        List<Report> reports = reportRepository.findByStatus("처리 전");

        // Member 엔티티를 MemberResDto로 매핑하여 리스트로 반환
        return reports.stream()
                .map(report -> {
                    ReportDto reportDto = modelMapper.map(report, ReportDto.class);
                    reportDto.setReporter(report.getReporter());
                    reportDto.setReported(report.getReported());
                    return reportDto;
                })
                .collect(Collectors.toList());
    }

    // 처리 후 신고 목록 조회
    public List<ReportDto> selectAfterReport() {
        List<Report> reports = reportRepository.findByStatusNot("처리 전");

        // Member 엔티티를 MemberResDto로 매핑하여 리스트로 반환
        return reports.stream()
                .map(report -> {
                    ReportDto reportDto = modelMapper.map(report, ReportDto.class);
                    reportDto.setReporter(report.getReporter());
                    reportDto.setReported(report.getReported());
                    return reportDto;
                })
                .collect(Collectors.toList());
    }

    // 이름으로 신고 목록 조회
    public List<ReportDto> selectReport(String name) {
        List<Report> reports = reportRepository.findByReporter_NameContainingOrReported_NameContaining(name,name);
        // Member 엔티티를 MemberResDto로 매핑하여 리스트로 반환
        return reports.stream()
                .map(report -> {
                    ReportDto reportDto = modelMapper.map(report, ReportDto.class);
                    reportDto.setReporter(report.getReporter());
                    reportDto.setReported(report.getReported());
                    return reportDto;
                })
                .collect(Collectors.toList());
    }

    // 닉네임으로 신고 목록 조회
    public List<ReportDto> selectReportNick(String name) {
        List<Report> reports = reportRepository.findByReporter_NickNameContainingOrReported_NickNameContaining(name,name);
        // Member 엔티티를 MemberResDto로 매핑하여 리스트로 반환
        return reports.stream()
                .map(report -> {
                    ReportDto reportDto = modelMapper.map(report, ReportDto.class);
                    reportDto.setReporter(report.getReporter());
                    reportDto.setReported(report.getReported());
                    return reportDto;
                })
                .collect(Collectors.toList());
    }
    // Id로 신고 목록 조회
    public List<ReportDto> selectReportId(String name) {
        List<Report> reports = reportRepository.findByReporter_MemberIdContainingOrReported_MemberIdContaining(name,name);
        // Member 엔티티를 MemberResDto로 매핑하여 리스트로 반환
        return reports.stream()
                .map(report -> {
                    ReportDto reportDto = modelMapper.map(report, ReportDto.class);
                    reportDto.setReporter(report.getReporter());
                    reportDto.setReported(report.getReported());
                    return reportDto;
                })
                .collect(Collectors.toList());
    }

}