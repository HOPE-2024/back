package com.hopeback.dto.member;

import com.hopeback.entity.member.Naver;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class NaverDto {
    private Long id;
    private String nickName;
    private String email;
    private String name;

    public Naver toEntity() {
        return Naver.builder()
                .id(id)
                .nickName(nickName)
                .email(email)
                .name(name)
                .build();
    }
}
