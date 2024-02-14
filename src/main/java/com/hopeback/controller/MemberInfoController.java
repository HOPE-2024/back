package com.hopeback.controller;

import com.hopeback.dto.member.MemberInfoDto;
import com.hopeback.service.MemberInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/member-info")
@RequiredArgsConstructor
public class MemberInfoController {

    private final MemberInfoService memberInfoService;

    @GetMapping("/{memberId}")
    public ResponseEntity<MemberInfoDto> getMemberInfo(@PathVariable String memberId) {
        MemberInfoDto memberInfoDto = memberInfoService.getMemberInfo(memberId);
        return new ResponseEntity<>(memberInfoDto, HttpStatus.OK);
    }

    @PutMapping("/modify/{memberId}")
    public ResponseEntity<Boolean> updateMemberInfo(@PathVariable String memberId, @RequestBody MemberInfoDto memberInfoDto) {
        log.info("유저 인포 업데이트 하기 : {}", memberId);
        boolean isTrue = memberInfoService.updateMemberInfo(memberId, memberInfoDto); // memberId와 memberInfoDto를 넘겨줍니다.
        log.info("유저 인포 업데이트 하기 : {}", memberId);
        return ResponseEntity.ok(isTrue);
    }
}