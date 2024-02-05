package com.hopeback.controller;

import com.hopeback.dto.admin.QueryDto;
import com.hopeback.dto.admin.ReplyDto;
import com.hopeback.dto.admin.ReportDto;
import com.hopeback.dto.member.MemberResDto;
import com.hopeback.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;


    // 모든 회원 조회
    @GetMapping("/list")
    public ResponseEntity<List<MemberResDto>> selectMemberList() {
        List<MemberResDto> list = adminService.selectMemberList();
        return ResponseEntity.ok(list);
    }
    // 채팅 정지 회원 조회
    @GetMapping("/chatting")
    public ResponseEntity<List<MemberResDto>> stopChattingList() {
        List<MemberResDto> list = adminService.stopChattingList();
        return ResponseEntity.ok(list);
    }
    // 계정 정지 회원 조회
    @GetMapping("/stopMember")
    public ResponseEntity<List<MemberResDto>> stopMemberList() {
        List<MemberResDto> list = adminService.stopMemberList();
        return ResponseEntity.ok(list);
    }

    // 이름으로 회원 조회
    @PostMapping("/memberName")
    public ResponseEntity<List<MemberResDto>>  selectMemberName(@RequestParam String name) {
        List<MemberResDto> list= adminService.selectMemberName(name);
        return ResponseEntity.ok(list);
    }

    // Id로 회원 조회
    @PostMapping("/memberId")
    public ResponseEntity<List<MemberResDto>>  selectMemberId(@RequestParam String name) {
        List<MemberResDto> list= adminService.selectMemberId(name);
        return ResponseEntity.ok(list);
    }

    // 닉네임으로 회원 조회
    @PostMapping("/memberNick")
    public ResponseEntity<List<MemberResDto>>  selectMemberNick(@RequestParam String name) {
        List<MemberResDto> list= adminService.selectMemberNick(name);
        return ResponseEntity.ok(list);
    }

    // 회원  정지
    @PostMapping("/active")
    public ResponseEntity<Boolean> updateActive(@RequestBody MemberResDto memberResDto) {
        Boolean list = adminService.updateActive(memberResDto);
        return ResponseEntity.ok(list);
    }


    // 모든 신고 목록 조회
    @GetMapping("/report")
    public ResponseEntity<List<ReportDto>> selectReportList() {
        List<ReportDto> list = adminService.selectReportList();
        return ResponseEntity.ok(list);
    }

    // 신고 삭제
    @PostMapping("/deletereport")
    public ResponseEntity<Boolean>  deleteReport(@RequestParam Long id) {
        Boolean list= adminService.deleteReport(id);
        return ResponseEntity.ok(list);
    }



    // 신고 상태 변경(읽음)
    @PostMapping("/updateReportStatus")
    public ResponseEntity<Boolean>  updateReportStatus(@RequestParam Long id) {
        Boolean list= adminService.updateReportStatus(id);
        return ResponseEntity.ok(list);
    }

    // 신고 상태 변경(처리 상황)
    @PostMapping("/ReportActive")
    public ResponseEntity<Boolean> updateReportStatus(@RequestBody ReportDto reportDto) {
        Boolean list = adminService.updateReportStatus(reportDto);
        return ResponseEntity.ok(list);
    }

    // 처리 전 신고 목록 조회
    @GetMapping("/beforereport")
    public ResponseEntity<List<ReportDto>> selectBeforeReport() {
        List<ReportDto> list = adminService.selectBeforeReport();
        return ResponseEntity.ok(list);
    }

    // 처리 후 신고 목록 조회
    @GetMapping("/afterreport")
    public ResponseEntity<List<ReportDto>> selectAfterReport() {
        List<ReportDto> list = adminService.selectAfterReport();
        return ResponseEntity.ok(list);
    }
    // 이름으로 신고 목록 조회
    @PostMapping("/selectReport")
    public ResponseEntity<List<ReportDto>>  selectReport(@RequestParam String name) {
        List<ReportDto> list= adminService.selectReport(name);
        return ResponseEntity.ok(list);
    }
    // 닉네임으로 신고 목록 조회
    @PostMapping("/selectReportNick")
    public ResponseEntity<List<ReportDto>>  selectReportNick(@RequestParam String name) {
        List<ReportDto> list= adminService.selectReportNick(name);
        return ResponseEntity.ok(list);
    }
    // 아이디로 신고 목록 조회
    @PostMapping("/selectReportId")
    public ResponseEntity<List<ReportDto>>  selectReportId(@RequestParam String name) {
        List<ReportDto> list= adminService.selectReportId(name);
        return ResponseEntity.ok(list);
    }

    // 1대1 문의 등록
    @PostMapping("/insertQuery")
    public ResponseEntity<Boolean>  selectReportId(@RequestBody QueryDto queryDto) {
        Boolean list= adminService.insertQuery(queryDto);
        return ResponseEntity.ok(list);
    }

    // 1대1 문의 모두 조회
    @GetMapping("/selectQuryList")
    public ResponseEntity<List<QueryDto>> selectQuryList() {
        List<QueryDto> list = adminService.selectQuryList();
        return ResponseEntity.ok(list);
    }

    // 1대1 문의  닉네임 조회
    @GetMapping("/nickNameSelectQuryList")
    public ResponseEntity<List<QueryDto>> nickNameSelectQuryList() {
        List<QueryDto> list = adminService.nickNameSelectQuryList();
        return ResponseEntity.ok(list);
    }

    // 1대1 문의 하나 조회
    @GetMapping("/selectQury")
    public ResponseEntity <QueryDto> selectQury(@RequestParam Long id) {
        QueryDto list = adminService.selectQury(id);
        return ResponseEntity.ok(list);
    }


    // 문의 글에 댓글 추가
    @PostMapping("/InsertReply")
    public ResponseEntity <Boolean> InsertReply(@RequestBody ReplyDto replyDto) {
        Boolean list = adminService.InsertReply(replyDto);
        return ResponseEntity.ok(list);
    }

    // 신고 삭제
    @PostMapping("/deleteReply")
    public ResponseEntity<Boolean>  deleteReply(@RequestParam Long id) {
        Boolean list= adminService.deleteReply(id);
        return ResponseEntity.ok(list);
    }
    // 문의 글 삭제
    @PostMapping("/deleteQuery")
    public ResponseEntity<Boolean>  deleteQuery(@RequestParam Long id) {
        Boolean list= adminService.deleteQuery(id);
        return ResponseEntity.ok(list);
    }
    //댓글 수정
    @PostMapping("/updateReply")
    public ResponseEntity <Boolean> updateReply(@RequestBody ReplyDto replyDto) {
        Boolean list = adminService.updateReply(replyDto);
        return ResponseEntity.ok(list);
    }


    // 1대1 문의  수정
    @PostMapping("/updateQuery")
    public ResponseEntity<Boolean>  updateQuery(@RequestBody QueryDto queryDto) {
        Boolean list= adminService.updateQuery(queryDto);
        return ResponseEntity.ok(list);
    }
}

