package com.hopeback.controller;

import com.hopeback.dto.admin.QueryDto;
import com.hopeback.dto.admin.ReplyDto;
import com.hopeback.dto.admin.ReportDto;
import com.hopeback.dto.member.MemberDto;
import com.hopeback.dto.member.MemberResDto;
import com.hopeback.security.SecurityUtil;
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
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // 모든 회원 조회
    @GetMapping("/list")
    public ResponseEntity<List<MemberDto>> selectMemberList() {
        List<MemberDto> list = adminService.selectMemberList();
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

    // 모든 회원 페이지네이션으로 정보 가져가기
    @GetMapping("/member/page")
    public ResponseEntity<List<MemberDto>>selectMemberPageList(@RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "10") int size) {
        List<MemberDto> list = adminService.selectMemberPageList(page, size);
        return ResponseEntity.ok(list);
    }
    // 모든 회원 페이지 수 조회
    @GetMapping("/member/count")
    public ResponseEntity<Integer> memberPage(@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        int pageCnt = adminService.memberPage(pageRequest);
        return ResponseEntity.ok(pageCnt);
    }

    // 채팅 정지 회원 페이지네이션으로 정보 가져가기
    @GetMapping("/chatting/page")
    public ResponseEntity<List<MemberResDto>>chatingMembers(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size) {
        List<MemberResDto> list = adminService.chatingMembers(page, size);
        return ResponseEntity.ok(list);
    }
    // 채팅 정지 회원 페이지 수 조회
    @GetMapping("/chatting/count")
    public ResponseEntity<Integer> chatingMemberPage(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        int pageCnt = adminService.chatingMembersPage(pageRequest);
        return ResponseEntity.ok(pageCnt);
    }

    // 정지 회원 페이지네이션으로 정보 가져가기
    @GetMapping("/stop/page")
    public ResponseEntity<List<MemberResDto>>stopMember(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size) {
        List<MemberResDto> list = adminService.stopMember(page, size);
        return ResponseEntity.ok(list);
    }
    // 정지 회원 페이지 수 조회
    @GetMapping("/stop/count")
    public ResponseEntity<Integer> stopMemberPage(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        int pageCnt = adminService.stopMemberPage(pageRequest);
        return ResponseEntity.ok(pageCnt);
    }

}

