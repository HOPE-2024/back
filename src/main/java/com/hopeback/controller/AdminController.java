package com.hopeback.controller;

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
    // 정지 회원 조회
    @GetMapping("/chatting")
    public ResponseEntity<List<MemberResDto>> stopChattingList() {
        List<MemberResDto> list = adminService.stopChattingList();
        return ResponseEntity.ok(list);
    }
    // 정지 회원 조회
    @GetMapping("/stopMember")
    public ResponseEntity<List<MemberResDto>> stopMemberList() {
        List<MemberResDto> list = adminService.stopMemberList();
        return ResponseEntity.ok(list);
    }

    // 회원 조회
    @PostMapping("/member")
    public ResponseEntity<List<MemberResDto>>  selectMember(@RequestParam String name) {
        List<MemberResDto> list= adminService.selectMember(name);
        return ResponseEntity.ok(list);
    }

    // 회원  정지
    @PostMapping("/active")
    public ResponseEntity<Boolean> updateActive(@RequestBody MemberResDto memberResDto) {
        Boolean list = adminService.updateActive(memberResDto);
        return ResponseEntity.ok(list);
    }

}

