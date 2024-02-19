package com.hopeback.service.jwt;


import com.hopeback.dto.jwt.TokenDto;
import com.hopeback.dto.member.MemberReqDto;
import com.hopeback.dto.member.MemberResDto;
import com.hopeback.entity.jwt.RefreshToken;
import com.hopeback.entity.member.Member;
import com.hopeback.jwt.TokenProvider;
import com.hopeback.repository.MemberRepository;
import com.hopeback.repository.jwt.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder managerBuilder;  // 인증 담당 클래스
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    // 중복 체크(id, nickName)
    public Boolean checkUnique(int type, String info) {
        boolean isUnique;
        switch (type) {
            case 0:
                isUnique = memberRepository.existsByMemberId(info);
                break;
            case 1:
                isUnique = memberRepository.existsByNickName(info);
                break;
            default:
                isUnique = true;
                log.info("중복 체크 잘못 됨!!");
        }
        return isUnique;
    }

    // 회원 가입
    public MemberResDto signup(MemberReqDto memberReqDto, String verificationCode) {
        if (memberRepository.existsByMemberId(memberReqDto.getMemberId())) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }
        Member member = memberReqDto.toEntity(passwordEncoder);
        return MemberResDto.of(memberRepository.save(member));
    }

    // 로그인
    public TokenDto login(MemberReqDto memberReqDto) {
        // 사용자가 입력한 로그인 객체의 정보(requestDto)를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = memberReqDto.toAuthentication();
        log.info("authenticationToken : {}", authenticationToken);

        // 인증 성공시 Authentication 객체 반환됨
        Authentication authentication = managerBuilder.getObject().authenticate(authenticationToken);
        log.info("authentication 객체: {}", authentication);
        // 인증 정보를 바탕으로 토큰 생성, TokenDto에 생성된 토큰 정보 들어있음
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal(); // getPrincipal : 인증 객체에서 주체 정보를 추출, 아이디, 권한 정보등 비밀번호와 같이 보호되어야 하는 정보 이외의 회원정보가 모두 저장된 객체
        log.warn("'userDetails' : " + String.valueOf(userDetails));

        // refreshToken DB에 저장을 위해 사용자 ID를 사용하여 데이터베이스에서 해당 사용자를 조회
        // userDetails.getUsername() : memberId (aaa 같은)
        String username = userDetails.getUsername();
        Member member = memberRepository.findByMemberId(username)
                .orElseThrow(() -> new RuntimeException("해당 유저가 존재하지 않습니다."));
        log.warn("조회된 회원 : " + member);

        // 조회된 회원을 기반으로 리프레시 토큰 DB 저장, 이미 존재하는 경우에는 갱신
        String refreshToken = tokenDto.getRefreshToken();
        RefreshToken retriedRefreshToken = refreshTokenRepository
                .findByMember(member)
                .orElse(null);

        if (retriedRefreshToken == null) {
            // 리프레시 토큰 존재X, 새로 발급
            RefreshToken newRefreshToken = RefreshToken.builder()
                    .refreshToken(refreshToken)
                    .refreshTokenExpiresIn(tokenDto.getRefreshTokenExpiresIn())
                    .member(member)
                    .build();
            refreshTokenRepository.save(newRefreshToken);
            log.warn("리프레시 토큰 새로 발급 !!");

        } else {
            // 갱신
            log.info("이미 존재하는 리프레시 토큰: {}", retriedRefreshToken.getRefreshToken());
            retriedRefreshToken.update(refreshToken, tokenDto.getRefreshTokenExpiresIn());
            log.warn("리프레시 토큰 이미 존재해서 업데이트 !!");
            
        }

        // 생성된 토큰 반환
        return tokenDto;
    }

    // 리프레시 토큰의 유효성 검증 후, 유효하면 액세스 토큰 반환
    public TokenDto refreshAccessToken(String refreshToken) {
        try {  // 리프레시 토큰의 유효성 확인
            if (tokenProvider.validateToken(refreshToken)) {
                // 유효한 리프레시 토큰을 가져와 사용자의 인증 정보를 가져와 새로운 토큰 생성. 정보는 TokenDto 객체에 저장
                TokenDto tokenDto = tokenProvider.generateTokenDto(tokenProvider.getAuthentication(refreshToken));

                log.info("refreshAccessToken tokenDto 액세스 토큰 : {}", tokenDto.getAccessToken());
                log.info("refreshAccessToken tokenDto 리프레스 토큰 : {}", tokenDto.getRefreshToken());

                // 데이터베이스에서 해당 리프레시 토큰 정보를 찾음. 존재하지 않으면 에러 메세지 발생.
                RefreshToken retriedRefreshToken = refreshTokenRepository
                        .findByRefreshToken(refreshToken.substring(0, refreshToken.length() - 1))
                        .orElseThrow(() -> new RuntimeException("해당 리프레시 토큰이 존재하지 않습니다."));
                // 새롭게 생성된 토큰 정보를 이용하여 기존의 리프레시 토큰을 갱신
                retriedRefreshToken.update(tokenDto.getRefreshToken(), tokenDto.getRefreshTokenExpiresIn());

                return tokenDto;
            } else {
                log.info("액세스 토큰 줘라!!");
            }
        } catch (RuntimeException e) {
            log.info("refreshAccessToken 리프레시 토큰 : {}", refreshToken);
            log.error("refreshAccessToken 유효성 검증 중 예외 발생 : {}", e.getMessage());
        }
        return null;
    }


}
