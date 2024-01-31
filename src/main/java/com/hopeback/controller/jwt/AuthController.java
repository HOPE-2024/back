package com.hopeback.controller.jwt;

import antlr.Token;
import com.hopeback.dto.jwt.TokenDto;
import com.hopeback.dto.member.MemberReqDto;
import com.hopeback.dto.member.MemberResDto;
import com.hopeback.service.jwt.AuthService;
import com.hopeback.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    // 중복 체크
    @PostMapping("/isUnique")
    public ResponseEntity<Boolean> isUnique(@RequestBody Map<String, String> dataMap) {
        int type = Integer.parseInt(dataMap.get("type"));
        return ResponseEntity.ok(authService.checkUnique(type, dataMap.get("data")));
    }

    // 회원 가입
    @PostMapping("/signup")
    public ResponseEntity<MemberResDto> signup(@RequestBody MemberReqDto requestDto) {
        System.out.println("가입 정보 : " + requestDto);
        return ResponseEntity.ok(authService.signup(requestDto));
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody MemberReqDto memberReqDto) {
        System.out.println("회원 ID : " + memberReqDto.getMemberId() );
        return ResponseEntity.ok(authService.login(memberReqDto));
    }
}
