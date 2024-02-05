package com.hopeback.controller;

import com.hopeback.dto.member.MemberDto;
import com.hopeback.dto.member.MemberMyPageDto;
import com.hopeback.dto.member.MemberResDto;
import com.hopeback.security.SecurityUtil;
import com.hopeback.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final MemberDto memberDto;

    @Autowired   // Autowired : 필드, 생성자, 세터 메서드에 해당하는 타입의 빈을 찾아 자동으로 주입
    public MemberController(MemberService memberService, MemberDto memberDto) {
        this.memberService = memberService;
        this.memberDto = memberDto;
    }

    // 회원 상세 조회
    @GetMapping("/detail")
    public ResponseEntity<MemberResDto> memberDetail() {
        String memberId = SecurityUtil.getCurrentMemberId();
        MemberResDto memberResDto = memberService.getMemberDetail(memberId);
        return ResponseEntity.ok(memberResDto);
    }

}
