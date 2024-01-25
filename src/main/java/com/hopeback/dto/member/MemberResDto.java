package com.hopeback.dto.member;

import com.hopeback.entity.member.Member;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberResDto {
    // 회원 조회 등 클라이언트로 전송하기 위한 용도
    private Long id;
    private String memberId;
    private String password;
    private String name;
    private String nickName;
    private String email;
    private String phone;
    private Integer active;
    private LocalDateTime active_date;

    // Member -> MemberResDto
    // Member 엔티티의 필요한 일부 필드만을 가져와서 전달. 응답용 Dto 생성
    // 클리언트에 반환되는 정보를 최소화하고, 필요한 정보만을 전송함으로써 불필요한 데이터 전송을 피하고 성능 향상
    public static MemberResDto of(Member member) {
        return  MemberResDto.builder()
                .id(member.getId())
                .memberId(member.getMemberId())
                .password(member.getPassword())
                .name(member.getName())
                .nickName(member.getNickName())
                .email(member.getEmail())
                .phone(member.getPhone())
                .build();
    }


}
