package com.hopeback.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hopeback.constant.Authority;
import com.hopeback.dto.jwt.TokenDto;
import com.hopeback.dto.member.MemberDto;
import com.hopeback.dto.member.MemberResDto;
import com.hopeback.security.SecurityUtil;
import com.hopeback.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.json.JsonException;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final MemberDto memberDto;
//    private final NaverService naverService;

    @Autowired   // Autowired : 필드, 생성자, 세터 메서드에 해당하는 타입의 빈을 찾아 자동으로 주입
    public MemberController(MemberService memberService, MemberDto memberDto) {
        this.memberService = memberService;
        this.memberDto = memberDto;
    }

    // 회원 상세 조회
    @GetMapping("/detail")
    public ResponseEntity<MemberResDto> memberDetail() {
        String memberId = SecurityUtil.getCurrentMemberId();
        MemberResDto memberResDto = memberService.getMemberDetail(memberId);
        return ResponseEntity.ok(memberResDto);
    }

    // 카카오 회원가입 및 로그인
    @PostMapping("/kakaoLogin")
    public ResponseEntity<TokenDto> kakaoLogin(@RequestBody Map<String, Object> kakaoData) throws JsonException, JsonProcessingException {
        // json은 키-값 쌍으로 구성된 데이터를 표현. object 타입을 사용함으로써 다양한 데이터 타입을 유연하게 처리할 수 있음.
        // 따라서 Map<String, Object> 타입은 JSON 형식의 데이터를 매개변수로 받기 위해 사용되며, 다양한 키와 그에 해당하는 값을 동적으로 처리할 수 있는 유연성을 제공.

        log.warn("kakaoData 타입: " + kakaoData.getClass().getName());

        // "access_token" 이라는 이름의 key는 OAuth2.0 인증 방식에서 사용하는 표준 키
        Map<String, Object> nestedMap = (Map<String, Object>) kakaoData.get("kakaoData");
        String kakaoToken = "";
        if (nestedMap != null) {
            kakaoToken = (String) nestedMap.get("access_token");
            log.warn("카카오 액세스 토큰 : " + kakaoToken);
        }

        if (kakaoToken == null || kakaoToken.isEmpty()) {
            log.error("controller kakaoLogin Null!!!");
            return null;
        }

        // kakaoToken을 통해 카카오 서버에서 사용자 정보 조회
        String kakaoUserInfo = memberService.requestKakaoUserInfo(kakaoToken);

        // JSON 파싱
        JSONObject jsonObject = new JSONObject(kakaoUserInfo);

        // 조회된 사용자 정보를 JSON 객체로 변환 :  JSON 객체에서 "properties" 키 아래의 "nickname"과 "profile_image" 값을 추출
        String kakaoNickName = jsonObject.getJSONObject("properties").get("nickname").toString();
        String kakaoProfileImageUrl = jsonObject.getJSONObject("properties").get("profile_image").toString();

        // 회원 정보 조회 및 처리
        if(!memberService.kakaoSignupCheck(kakaoNickName)) {
            // 회원 가입 후 로그인 처리
            memberDto.setNickName(kakaoNickName);
            memberDto.setAuthority(Authority.MEMBER);
            memberDto.setImage(kakaoProfileImageUrl);
            memberService.kakaoSignup(memberDto);
        }
        // 이미 가입되어 있는 경우 (또는 방금 가입한 경우) 로그인 처리
        TokenDto token = memberService.kakaoLogin(kakaoNickName);
        return ResponseEntity.ok(token);
    }

//    // 네이버 로그인
//    @PostMapping("/naverlogin")
//    public void naverLogin(HttpServletRequest request, HttpServletResponse response) throws MalformedURLException, UnsupportedEncodingException, URISyntaxException {
//            String url = memberService.getNaverAuthorizeUrl("authorize");
//            try {
//                response.sendRedirect(url);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//    }

    // 네이버 회원가입 여부
//    @PostMapping("/navermember")
//    public ResponseEntity<Map<String, Object>> naverMember(@RequestBody String naverToken) {
//        log.info("네이버 토큰 : {}", naverToken);
//        Map<String, Object> response = naverService.naverInfo(naverToken);
//        log.warn("naver rsp : {}", response);
//        return ResponseEntity.ok(response);
//    }

}
