package com.hopeback.dto.member;

import com.hopeback.constant.Authority;
import com.hopeback.entity.member.Member;
import lombok.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberReqDto {
    // 회원가입 요청 등 클라이언트로부터 전달된 데이터를 받음
    private Long id;
    private String memberId;
    private String password;
    private String name;
    private String email;
    private String nickName;
    private String phone;
    private boolean marketingAgree;

    // MemberResDto -> Member
    public Member toEntity(PasswordEncoder passwordEncoder){  // 비밀번호 암호화
        return Member.builder()
                .memberId(memberId)
                .password(passwordEncoder.encode(password)) // 암호화
                .name(name)
                .email(email)
                .nickName(nickName)
                .phone(phone)
                .authority(Authority.MEMBER)
                .build();
    }
    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(memberId, password);
    }
}
