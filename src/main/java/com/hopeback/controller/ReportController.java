package com.hopeback.controller;

import com.hopeback.dto.admin.QueryDto;
import com.hopeback.dto.admin.ReplyDto;
import com.hopeback.dto.admin.ReportDto;
import com.hopeback.dto.member.MemberResDto;
import com.hopeback.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {
    private final AdminService adminService;



    // 회원  정지
    @PostMapping("/active")
    public ResponseEntity<Boolean> updateActive(@RequestBody MemberResDto memberResDto) {
        Boolean list = adminService.updateActive(memberResDto);
        return ResponseEntity.ok(list);
    }


    // 모든 신고 목록 조회
    @GetMapping("/reporList")
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


    // 신고 추가
    @PostMapping("/insertReport")
    public ResponseEntity<Boolean> insertReport(@RequestBody ReportDto reportDto) {
        Boolean list= adminService.insertReport(reportDto);
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

    // 1대1 문의 조회
    @GetMapping("/selectQueryList")
    public ResponseEntity<List<QueryDto>> selectQueryList() {
        List<QueryDto> list = adminService.selectQueryList();
        return ResponseEntity.ok(list);
    }

    //내 문의 글 조회
    @GetMapping("/selectMyQuery")
    public ResponseEntity<List<QueryDto>> selectMyQuery() {
        List<QueryDto> list = adminService.selectMyQuery();
        return ResponseEntity.ok(list);
    }

    // 1대1 문의 하나 조회
    @GetMapping("/selectQuery")
    public ResponseEntity <QueryDto> selectQuery(@RequestParam Long id) {
        QueryDto list = adminService.selectQuery(id);
        return ResponseEntity.ok(list);
    }


    // 문의 글에 댓글 추가
    @PostMapping("/insertReply")
    public ResponseEntity <Boolean> insertReply(@RequestBody ReplyDto replyDto) {
        Boolean list = adminService.insertReply(replyDto);
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


    // 페이지네이션으로 가져가기
    @GetMapping("/listA/page")
    public ResponseEntity<List<ReportDto>>reportList(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size) {
        List<ReportDto> list = adminService.reportList(page, size);
        return ResponseEntity.ok(list);
    }
    // 페이지 수 조회
    @GetMapping("/listA/count")
    public ResponseEntity<Integer> reportListCount(@RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size) {

        PageRequest pageRequest = PageRequest.of(page, size);
        int pageCnt = adminService.reportListCount(pageRequest);
        return ResponseEntity.ok(pageCnt);
    }

    // 페이지네이션으로 처리전 데이터 출력
    @GetMapping("/beforelist/page")
    public ResponseEntity<List<ReportDto>> beforeReportList(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size) {
        List<ReportDto> list = adminService.beforeReportList(page, size);

        return ResponseEntity.ok(list);
    }
    // 처리 전 페이지 수 조회
    @GetMapping("/beforelist/count")
    public ResponseEntity<Integer>  beforeReportPage(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        int pageCnt = adminService.beforeReportPage(pageable);
        return ResponseEntity.ok(pageCnt);
    }

    // 페이지네이션으로 처리 후 데이터 출력
    @GetMapping("/afterlist/page")
    public ResponseEntity<List<ReportDto>> afterReportList(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size) {

        List<ReportDto> list = adminService.afterReportList(page, size);
        return ResponseEntity.ok(list);
    }
    // 처리 후 페이지 수 조회
    @GetMapping("/afterlist/count")
    public ResponseEntity<Integer>  afterReportPage(@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        int pageCnt = adminService.afterReportPage(pageable);
        return ResponseEntity.ok(pageCnt);

    }
}

