package com.hopeback.controller;

import com.hopeback.dto.member.MemberMyPageDto;
import com.hopeback.service.MemberMyPageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberMyPageController {
    private final MemberMyPageService memberMyPageService;

    //회원 상세조회(마이페이지)
    @GetMapping("/detail/{email}")
    public ResponseEntity<MemberMyPageDto> userDetail(@PathVariable String email) {
        log.info("상세조회 이메일이 제대로 들어왔는지 확인 : {}", email);
        MemberMyPageDto memberMyPageDto = memberMyPageService.getMemberDetail(email);
        log.info("userMyPageDto controller 회원상세조회 닉네임 들어오는지 확인 : {}", memberMyPageDto.getNickName());
        return ResponseEntity.ok(memberMyPageDto);
    }
}
