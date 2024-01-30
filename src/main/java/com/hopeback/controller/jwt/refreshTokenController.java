package com.hopeback.controller.jwt;

import com.hopeback.dto.jwt.TokenDto;
import com.hopeback.service.jwt.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/refresh")
@RequiredArgsConstructor
public class refreshTokenController {
    private final AuthService authService;

    // accessToken 재발급
    @PostMapping("/new")
    public ResponseEntity<TokenDto> newToken(@RequestBody String refreshToken) {
        log.info("accessToken 재발급을 위한 refreshToken : {}", refreshToken);
        return ResponseEntity.ok(authService.refreshAccessToken(refreshToken));
    }
}
