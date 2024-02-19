//package com.hopeback.service;
//
//import com.hopeback.dto.member.NaverDto;
//import com.hopeback.entity.member.Naver;
//import com.hopeback.repository.MemberRepository;
//import com.hopeback.repository.NaverRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import org.springframework.http.HttpHeaders;
//import java.util.HashMap;
//import java.util.Map;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//@Transactional
//public class NaverService {
//    // RestTemplate : 외부 API와 통실할 때 사용. RESTful 서비스와 상호 작용하거나, 외부 서비스와 데이터를 교환할 때 사용하여 HTTP 통신 처리.
//
//    @Autowired
//    private final RestTemplate restTemplate;
//    private final MemberRepository memberRepository;
//    private final NaverRepository naverRepository;
//
////    // 네이버 로그인
////
////    public String getNaverAuthorizeUrl(String type) throws URISyntaxException, MalformedURLException, UnsupportedEncodingException {
////
////        String naverClientId = env.getProperty("spring.security.oauth2.client.registration.naver.client-id");
////        String naverClientSecret = env.getProperty("spring.security.oauth2.client.registration.naver.client-secret");
////        String naverScope = env.getProperty("spring.security.oauth2.client.registration.naver.scope");
////        String redirectUrl = env.getProperty("spring.security.oauth2.client.registration.naver.redirect-uri");
////
////        String naverAuthorizeUrl = env.getProperty("spring.security.oauth2.client.provider.naver.authorization-uri");
////
////        UriComponents uriComponents = UriComponentsBuilder
////                .fromUriString(naverAuthorizeUrl)
////                .queryParam("response_type", "code")
////                .queryParam("client_id", naverClientId)
////                .queryParam("redirect_uri", URLEncoder.encode(redirectUrl, "UTF-8"))
////                .queryParam("state", URLEncoder.encode("1234", "UTF-8"))
////                .build();
////
////        return uriComponents.toString();
////    }
//
//    // 네이버 로그인
//    public Map<String, Object> naverInfo(String naverToken) {
//        Map<String, Object> naverInfo = new HashMap<>();
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
//        headers.set("Authorization", "Bearer " + naverToken);
//
//        String url = "https://openapi.naver.com/v1/nid/me";
//
//        try {
//            ResponseEntity<NaverDto> responseEntity = restTemplate.exchange(
//                    url,
//                    HttpMethod.GET,
//                    new HttpEntity<>(headers),
//                    NaverDto.class
//            );
//            NaverDto naverDto = responseEntity.getBody();
//            boolean existence = false;
//            if(naverDto != null) {
//                existence = naverRepository.existsById(naverDto.getId());
//                log.info("naver email 존재 여부 ! : {}", existence);
//
//                String naverEmail = naverDto.getEmail();
//                if(memberRepository.existsByEmail(naverEmail) && !existence) {
//                    log.error("이미 가입된 네이버 메일 주소 입니다.");
//                    throw new RuntimeException("네이버 : 이미 가입된 메일 주소 입니다.");
//                }
//
//                if(!existence) saveNaverEntity(naverDto);
//                else {
//                    existence = memberRepository.existsByEmail(naverDto.getEmail());
//                }
//            }
//            naverInfo.put("isMember", existence);
//            naverInfo.put("userInfo", naverDto);
//
//            return naverInfo;
//        } catch (Exception e) {
//            log.error("네이버 가입 시도 중 오류 발생 !!!");
//            return null;
//        }
//
//    }
//
//    private void saveNaverEntity(NaverDto naverDto) {
//        Naver naver = naverDto.toEntity();
//        naverRepository.save(naver);
//    }
//
//
//}
