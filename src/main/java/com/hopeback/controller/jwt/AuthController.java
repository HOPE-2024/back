package com.hopeback.controller.jwt;

import antlr.Token;
import com.hopeback.dto.jwt.TokenDto;
import com.hopeback.dto.member.MemberReqDto;
import com.hopeback.dto.member.MemberResDto;
import com.hopeback.service.EmailService;
import com.hopeback.service.jwt.AuthService;
import com.hopeback.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final MemberService memberService;
    private final EmailService emailService;

    // 중복 체크
    @PostMapping("/isUnique")
    public ResponseEntity<Boolean> isUnique(@RequestBody Map<String, String> dataMap) {
        int type = Integer.parseInt(dataMap.get("type"));
        return ResponseEntity.ok(authService.checkUnique(type, dataMap.get("data")));
    }

    // 회원 가입
    @PostMapping("/signup")
    public ResponseEntity<MemberResDto> signup(@RequestBody MemberReqDto requestDto) {
        try {
            // 이메일을 보내고, 생성된 인증 코드를 받아옴
            String verificationCode = emailService.sendSimpleMessage(requestDto.getEmail());

            // 받아온 인증 코드와 함께 회원가입 처리
            MemberResDto response = authService.signup(requestDto, verificationCode);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // 이메일 보내기 실패 시 예외 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody MemberReqDto memberReqDto) {
        System.out.println("회원 ID : " + memberReqDto.getMemberId() );
        return ResponseEntity.ok(authService.login(memberReqDto));
    }

}

