package com.hopeback.controller;

import com.hopeback.dto.member.MemberMyPageDto;
import com.hopeback.service.MemberMyPageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/myPage")
@RequiredArgsConstructor
public class MemberMyPageController {
    private final MemberMyPageService memberMyPageService;

    //회원 상세조회(마이페이지)
    @GetMapping("/detail/{memberId}")
    public ResponseEntity<MemberMyPageDto> memberDetail(@PathVariable String memberId) {
        log.warn("상세조회 이메일이 제대로 들어왔는지 확인!!!! : {}", memberId);
        MemberMyPageDto memberMyPageDto = memberMyPageService.getMemberDetail(memberId);
        log.info("userMyPageDto controller 회원상세조회 닉네임 들어오는지 확인 : {}", memberMyPageDto.getNickName());
        return ResponseEntity.ok(memberMyPageDto);
    }

    //회원수정
    @PutMapping("/modify")
    public ResponseEntity<Boolean> modifyMemberInfo(@RequestBody MemberMyPageDto memberMyPageDto) {
        boolean isTrue = memberMyPageService.modifyMember(memberMyPageDto);
        log.warn("멤버 프로필 잘 받아오나 ?? {}", memberMyPageDto.getMemberId());
        log.warn("멤버 프로필 잘 받아오나 ?? {}", memberMyPageDto.getNickName());
        return ResponseEntity.ok(isTrue);
    }
}
