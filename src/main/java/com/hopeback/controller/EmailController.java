package com.hopeback.controller;

import com.hopeback.service.EmailService;
import com.hopeback.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;

@Slf4j
@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;
    private final MemberService memberService;



    // 이메일 인증 코드 발송
    @PostMapping("/code")
    public ResponseEntity<String> emailCode(@RequestParam String email) throws Exception {
        String code = emailService.sendSimpleMessage(email);
        log.info("프론트로 보내고 있는 인증 코드 : " + code);
        return ResponseEntity.ok(code);
    }

    // 이메일로 아이디 찾기
    @PostMapping("/findidbyemail")
    public ResponseEntity<String> findIdByEamil(@RequestParam String email) {
        try {
            String memberId = emailService.findIdByEmail(email);
            return ResponseEntity.ok(memberId);
        } catch(EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // 토큰 없이 회원 아이디 상세 조회
    @GetMapping("/idcheck")
    public ResponseEntity<Boolean> checkMemberIdExists(@RequestParam("memberId") String memberid) {
        boolean exists = emailService.memberIdExists(memberid);
        return ResponseEntity.ok(exists);
    }

}
