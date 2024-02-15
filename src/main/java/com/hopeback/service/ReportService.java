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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {
    private final MemberRepository memberRepository;
    private final ReportRepository reportRepository;
    private final QueryRepository queryRepository;
    private final ReplyRepository replyRepository;
    private final ModelMapper modelMapper;

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
    @Transactional
    //신고 하기
    public Boolean insertReport(ReportDto reportDto){
        try{
            String memberId = SecurityUtil.getCurrentMemberId();
            Member member = memberRepository.findByMemberId(memberId).orElseThrow(
                    () -> new RuntimeException("해당 회원이 존재하지 않습니다."));
            Member member2 = memberRepository.findByNickName(reportDto.getReporter().getNickName())
                    .orElseThrow(() -> new RuntimeException("해당 회원이 존재하지 않습니다."));


            Report report = new Report();

            report.setReportId(member.getId());
            // 신고 당한 사람
            report.setReported(member2);
            //신고 한 사람
            report.setReporter(member);
            report.setCheck(reportDto.getCheck());
            LocalDateTime now = LocalDateTime.now();
            report.setDate(now);
            report.setStatus("읽기 전" );
            report.setReason(reportDto.getReason());
            reportRepository.save(report);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    @Transactional
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
    @Transactional
    //신고 내용 읽음으로 변경
    public Boolean updateReportStatus(Long id){
        Report report = reportRepository.findById(id).orElseThrow(
                () -> new RuntimeException("신고 내역이 없습니다."));
        if(report !=null){
            report.setStatus("읽은 후");
            return true;
        }else {
            return false;
        }
    }



    @Transactional
    //신고 목록에서 회원 상태 변경
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


    // 읽기 전 신고 목록 조회
    public List<ReportDto> selectBeforeReport() {
        List<Report> reports = reportRepository.findByStatus("읽기 전");

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

    // 읽은 후 신고 목록 조회
    public List<ReportDto> selectAfterReport() {
        List<Report> reports = reportRepository.findByStatusNot("읽기 전");

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


    // 페이지네이션으로 읽은 후 데이터 출력
    public List<ReportDto> afterReportList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Report> reports = reportRepository.findAllByStatusNot("읽기 전", pageable);
        return reports.stream()
                .map(report -> {
                    ReportDto reportDto = modelMapper.map(report, ReportDto.class);
                    reportDto.setReporter(report.getReporter());
                    reportDto.setReported(report.getReported());
                    return reportDto;
                })
                .collect(Collectors.toList());
    }

    // 처리 후 페이지 후 조회
    public int afterReportPage(Pageable pageable){
        int count = reportRepository.countByStatusNot("읽기 전");
        return (int) Math.ceil((double) count / pageable.getPageSize());
    }


    // 페이지 수 조회
    public int reportListCount(Pageable pageable) {
        return reportRepository.findAll(pageable).getTotalPages();
    }

    // 페이지네이션으로 처리 전 데이터 출력
    public List<ReportDto> beforeReportList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Report> reports = reportRepository.findAllByStatus("읽기 전", pageable);
        return reports.stream()
                .map(report -> {
                    ReportDto reportDto = modelMapper.map(report, ReportDto.class);
                    reportDto.setReporter(report.getReporter());
                    reportDto.setReported(report.getReported());
                    return reportDto;
                })
                .collect(Collectors.toList());
    }


    // 처리 후 페이지 수 조회
    public int beforeReportPage(Pageable pageable){
        int count = reportRepository.countByStatus("읽기 전");
        return (int) Math.ceil((double) count / pageable.getPageSize());
    }


    // 페이지네이션으로  모든 데이터 출력
    public List<ReportDto> reportList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Report> reports = reportRepository.findAll(pageable).getContent();
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