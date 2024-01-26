package com.hopeback.service.jwt;

import com.hopeback.entity.member.Member;
import com.hopeback.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;


@Service
@RequiredArgsConstructor
// 사용자의 상세 정보를 불러와 로그인 인증을 처리하는 클래스
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {  // username 파라미터로 전달 받은 사용자Id를 가지고 DB에서 사용자 정보를 찾음
        return memberRepository.findByMemeberId(username)  // findByMemberId() 메서드를 호출하여 username에 해당하는 사용자 정보를 조회
                .map(this::createUserDetails)  // UserDetails 타입의 객체로 변환
                .orElseThrow(() -> new UsernameNotFoundException(username + " 을 DB에서 찾을 수 없습니다."));  // 사용자 정보가 없으면 예외 발생.
    }

    // DB에서 가져온 회원 정보를 UserDetails 타입으로 변환
    private UserDetails createUserDetails(Member member) {
        // Member 엔티티의 권한 정보를 문자열로 가져와 SimpleGrantedAuthority 객체를 생성
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getAuthority().toString());
        // User 객체를 생성
        return new User(
                String.valueOf(member.getMemberId()),
                member.getPassword(),
                Collections.singleton(grantedAuthority)
        );
    }  // Member 엔티티 정보를 기반으로 하여 인증에 필요한 UserDetails 객체를 생성하는 역할을 함.
}
