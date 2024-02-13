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
        List<String> activeList = List.of("채팅 7일 정지", "채팅 30일 정지");
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

    @Transactional

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
            report.setStatus("처리 전" );
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
            report.setStatus("읽음");
            return true;
        }else {
            return false;
        }
    }
    @Transactional
    //회원 상태 변경
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

    @Transactional
    //1대1 문의 등록
    public Boolean  insertQuery(QueryDto dto){
        try{
            String memberId = SecurityUtil.getCurrentMemberId();
            Member member = memberRepository.findByMemberId(memberId).orElseThrow(
                    () -> new RuntimeException("해당 회원이 존재하지 않습니다."));
            Query query = new Query();
            query.setQuestioner(member);
            query.setTitle(dto.getTitle());
            query.setDivision(dto.getDivision());
            query.setSubstance(dto.getSubstance());
            query.setQueryImg(dto.getQueryImg());
            if (member.getAuthority() == Authority.ADMIN) {
                log.warn("관리자 입니다.");
                query.setOften("자주 하는 질문");

            } else {
                log.warn("회원 입니다.");
                query.setOften("1대1 문의 ");
                query.setStatus("답변 전");
            }

            queryRepository.save(query);
            return true;
        }catch (Exception e){
            return false;
        }
    }


    //1대1 문의 전부 출력
    public List<QueryDto> selectQueryList() {
            List<Query> reports = queryRepository.findAll();
            return reports.stream()
                    .map(report -> {
                        QueryDto queryDto = modelMapper.map(report, QueryDto.class);
                        return queryDto;
                    })
                    .collect(Collectors.toList());
    }


    //내 문의 글 조회
    public List<QueryDto> selectMyQuery() {
        String memberId = SecurityUtil.getCurrentMemberId();
        Member member = memberRepository.findByMemberId(memberId).orElseThrow(
                () -> new RuntimeException("해당 회원이 존재하지 않습니다."));
//        List<Query> queries = queryRepository.findByQuestionerNickName(member.getNickName());
        List<Query> queries = queryRepository.findByQuestionerNickNameAndOftenNot(member.getNickName(), "자주 하는 질문");

        return queries.stream()
                .map(query -> modelMapper.map(query, QueryDto.class))
                .collect(Collectors.toList());
    }



    //1대1 문의 하나 출력
    public QueryDto selectQuery( Long id) {
        Query report = queryRepository.findById(id).orElseThrow(
                () -> new RuntimeException("해당 번호의 문의는 없습니다."));

        return modelMapper.map(report, QueryDto.class);
    }

    @Transactional
    //댓글 등록
    public Boolean  insertReply(ReplyDto dto){
        try{
            String memberId = SecurityUtil.getCurrentMemberId();
            Member member = memberRepository.findByMemberId(memberId).orElseThrow(
                    () -> new RuntimeException("해당 회원이 존재하지 않습니다."));
            Query query = queryRepository.findById(dto.getQueryId()).orElseThrow(
                    () -> new RuntimeException("해당 글이 존재하지 않습니다."));
            Reply reply = new Reply();
            reply.setAnswer(dto.getAnswer());
            reply.setAnswerer(member.getNickName());
            reply.setQuery(query);
            replyRepository.save(reply);
            if (member.getAuthority() == Authority.ADMIN) {
                log.warn("관리자 입니다.");
                query.setStatus("답변 완료");
                queryRepository.save(query);
            } else {
                log.warn("회원 입니다.");
            }

            return true;
        }catch (Exception e){
            log.warn(String.valueOf(e));
            return false;
        }
    }

    @Transactional
    //댓글 삭제
    public Boolean deleteReply(Long id){
        Reply reply = replyRepository.findById(id).orElseThrow(
                () -> new RuntimeException("댓글 내역이 없습니다."));
        if(reply !=null){
            replyRepository.deleteById(id);
            return true;
        }else {
            return false;
        }
    }
    @Transactional
    //문의글 삭제
    public Boolean deleteQuery(Long id){
        Query query = queryRepository.findById(id).orElseThrow(
                () -> new RuntimeException("신고 내역이 없습니다."));
        if(query !=null){
            queryRepository.deleteById(id);
            return true;
        }else {
            return false;
        }
    }
    @Transactional
    //댓글 수정
    public Boolean updateReply(ReplyDto replyDto) {
        Reply reply = replyRepository.findById(replyDto.getId()).orElseThrow(
                () -> new RuntimeException("댓글 내역이 없습니다."));
        if (reply != null) {
            reply.setAnswer(replyDto.getAnswer());
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    // 1대1 문의  수정
    public Boolean  updateQuery(QueryDto dto){
        try{
            Query query  = queryRepository.findById(dto.getId()).orElseThrow(
                    () -> new RuntimeException("해당 글이 존재하지 않습니다."));
            query.setDivision(dto.getDivision());
            query.setSubstance(dto.getSubstance());
            query.setTitle(dto.getTitle());
            if(dto.getQueryImg() != null){
                query.setQueryImg(dto.getQueryImg());
            }
            queryRepository.save(query);
            return true;
        }catch (Exception e){
            return false;
        }
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
    // 페이지 수 조회
    public int reportListCount(Pageable pageable) {
        return reportRepository.findAll(pageable).getTotalPages();
    }

    // 페이지네이션으로 처리 전 데이터 출력
    public List<ReportDto> beforeReportList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Report> reports = reportRepository.findAllByStatus("처리 전", pageable);
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
        int count = reportRepository.countByStatus("처리 전");
        return (int) Math.ceil((double) count / pageable.getPageSize());
    }


    // 페이지네이션으로 처리 후 데이터 출력
    public List<ReportDto> afterReportList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Report> reports = reportRepository.findAllByStatusNot("처리 전", pageable);
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
        int count = reportRepository.countByStatusNot("처리 전");
        return (int) Math.ceil((double) count / pageable.getPageSize());
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
        activeList.add("채팅 7일 정지");
        activeList.add("채팅 30일 정지");

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
        int count = memberRepository.countByActiveIn(List.of("채팅 7일 정지", "채팅 30일 정지"));
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
        // "처리 전" 상태에 있는 문의 글 조회
        List<Query> reports = queryRepository.findByOftenContaining("자주 하는 질문");

        // 조회된 각 문의 글을 QueryDto 객체로 매핑하여 리스트로 반환
        return reports.stream()
                .map(query -> modelMapper.map(query, QueryDto.class))
                .collect(Collectors.toList());
    }



}