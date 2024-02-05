package com.hopeback.controller;

import com.hopeback.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;


    // 이메일 인증 코드 발송
    @PostMapping("/code")
    public ResponseEntity<String> emailCode(@RequestParam String email) throws Exception {
        String code = emailService.sendSimpleMessage(email);
        log.info("프론트로 보내고 있는 인증 코드 : " + code);
        return ResponseEntity.ok(code);
    }

}
