package com.hopeback.controller;

import com.hopeback.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;


    // 이메일 인증 코드 발송
    @PostMapping("/code")
    public String emailCode(@RequestParam String email) throws Exception {
        String code = emailService.sendSimpleMessage(email);
        return code;
    }

}
