package com.hopeback.controller;

import com.hopeback.dto.admin.QueryDto;
import com.hopeback.dto.admin.ReplyDto;
import com.hopeback.dto.admin.ReportDto;
import com.hopeback.dto.member.MemberResDto;
import com.hopeback.service.AdminService;
import com.hopeback.service.ReportService;
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
    private final ReportService reportService;


    // 회원  정지
    @PostMapping("/active")
    public ResponseEntity<Boolean> updateActive(@RequestBody MemberResDto memberResDto) {
        Boolean list = adminService.updateActive(memberResDto);
        return ResponseEntity.ok(list);
    }


    // 모든 신고 목록 조회
    @GetMapping("/reporList")
    public ResponseEntity<List<ReportDto>> selectReportList() {
        List<ReportDto> list = reportService.selectReportList();
        return ResponseEntity.ok(list);
    }

    // 신고 삭제
    @PostMapping("/deletereport")
    public ResponseEntity<Boolean>  deleteReport(@RequestParam Long id) {
        Boolean list= reportService.deleteReport(id);
        return ResponseEntity.ok(list);
    }


    // 신고 추가
    @PostMapping("/insertReport")
    public ResponseEntity<Boolean> insertReport(@RequestBody ReportDto reportDto) {
        Boolean list= reportService.insertReport(reportDto);
        return ResponseEntity.ok(list);
    }


    // 신고 상태 변경(읽음)
    @PostMapping("/updateReportStatus")
    public ResponseEntity<Boolean>  updateReportStatus(@RequestParam Long id) {
        Boolean list= reportService.updateReportStatus(id);
        return ResponseEntity.ok(list);
    }

    // 신고 상태 변경(처리 상황)
    @PostMapping("/ReportActive")
    public ResponseEntity<Boolean> updateReportStatus(@RequestBody ReportDto reportDto) {
        Boolean list = reportService.updateReportStatus(reportDto);
        return ResponseEntity.ok(list);
    }

    // 읽기 전 신고 목록 조회
    @GetMapping("/beforereport")
    public ResponseEntity<List<ReportDto>> selectBeforeReport() {

        List<ReportDto> list = reportService.selectBeforeReport();
        return ResponseEntity.ok(list);
    }

    // 읽은 후 신고 목록 조회
    @GetMapping("/afterreport")
    public ResponseEntity<List<ReportDto>> selectAfterReport() {
        List<ReportDto> list = reportService.selectAfterReport();
        return ResponseEntity.ok(list);
    }
    // 이름으로 신고 목록 조회
    @PostMapping("/selectReport")
    public ResponseEntity<List<ReportDto>>  selectReport(@RequestParam String name) {
        List<ReportDto> list= reportService.selectReport(name);
        return ResponseEntity.ok(list);
    }
    // 닉네임으로 신고 목록 조회
    @PostMapping("/selectReportNick")
    public ResponseEntity<List<ReportDto>>  selectReportNick(@RequestParam String name) {
        List<ReportDto> list= reportService.selectReportNick(name);
        return ResponseEntity.ok(list);
    }
    // 아이디로 신고 목록 조회
    @PostMapping("/selectReportId")
    public ResponseEntity<List<ReportDto>>  selectReportId(@RequestParam String name) {
        List<ReportDto> list= reportService.selectReportId(name);
        return ResponseEntity.ok(list);
    }



    // 페이지네이션으로 가져가기
    @GetMapping("/listA/page")
    public ResponseEntity<List<ReportDto>>reportList(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size) {
        List<ReportDto> list = reportService.reportList(page, size);
        return ResponseEntity.ok(list);
    }
    // 페이지 수 조회
    @GetMapping("/listA/count")
    public ResponseEntity<Integer> reportListCount(@RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size) {

        PageRequest pageRequest = PageRequest.of(page, size);
        int pageCnt = reportService.reportListCount(pageRequest);
        return ResponseEntity.ok(pageCnt);
    }

    // 페이지네이션으로 처리전 데이터 출력
    @GetMapping("/beforelist/page")
    public ResponseEntity<List<ReportDto>> beforeReportList(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size) {
        List<ReportDto> list = reportService.beforeReportList(page, size);

        return ResponseEntity.ok(list);
    }



    // 처리 전 페이지 수 조회
    @GetMapping("/beforelist/count")
    public ResponseEntity<Integer>  beforeReportPage(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        int pageCnt = reportService.beforeReportPage(pageable);
        return ResponseEntity.ok(pageCnt);
    }

    // 페이지네이션으로 처리 후 데이터 출력
    @GetMapping("/afterlist/page")
    public ResponseEntity<List<ReportDto>> afterReportList(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size) {

        List<ReportDto> list = reportService.afterReportList(page, size);
        return ResponseEntity.ok(list);
    }
    // 처리 후 페이지 수 조회
    @GetMapping("/afterlist/count")
    public ResponseEntity<Integer>  afterReportPage(@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        int pageCnt = reportService.afterReportPage(pageable);
        return ResponseEntity.ok(pageCnt);

    }
}

