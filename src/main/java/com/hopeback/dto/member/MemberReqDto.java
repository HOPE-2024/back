package com.hopeback.dto.member;

import com.hopeback.constant.Authority;
import com.hopeback.entity.member.Member;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberReqDto {
    // 회원가입 요청 등 클라이언트로부터 전달된 데이터를 받음
    private Long id;
    private String member_Id;
    private String password;
    private String name;
    private String email;
    private String phone;

    // MemberResDto -> Member
    public Member toEntity(PasswordEncoder passwordEncoder){  // 비밀번호 암호화
        return Member.builder()
                .member_Id(member_Id)
                .password(passwordEncoder.encode(password)) // 암호화
                .name(name)
                .email(email)
                .phone(phone)
                .authority(Authority.ROLE_USER)
                .build();
    }

}
